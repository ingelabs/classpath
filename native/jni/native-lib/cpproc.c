/* cpproc.c -
   Copyright (C) 2003, 2004, 2005, 2006  Free Software Foundation, Inc.
   Copyright (C) 2026  INGELABS S.L.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

#include "config.h"
#include <jni.h>
#include <jcl.h>		/* for environ */
#include "cpproc.h"
#include "cpproc-child.h"
#include <signal.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <errno.h>
#include <fcntl.h>
#include <limits.h>
#include <stdlib.h>
#include <string.h>

#ifdef HAVE_POSIX_SPAWN
#include <spawn.h>
#ifndef CPPROC_SPAWN_HELPER
#error Path to the spawn helper not defined
#endif
#endif

/* Bound the last-resort fcntl scan when OPEN_MAX is pathologically large. */
#define MAX_FD_SCAN 65536

static void close_fds(int *fds, int numFds)
{
  int i;

  for (i = 0; i < numFds; i++)
    close(fds[i]);
}

/* Compute the fallback loop's upper bound in the parent. sysconf()
   is not async-signal-safe, so avoid calling it after fork. */
static int get_max_fd(void)
{
  long value = sysconf(_SC_OPEN_MAX);

  if (value <= 0 || value > MAX_FD_SCAN)
    return MAX_FD_SCAN;

  return (int) value;
}

/* Create a pipe with both endpoints above the standard descriptors.
   If fd 0, 1 or 2 is closed, pipe() would reuse the free slot, and
   the child's stdio setup would then close that fd or wire a
   standard descriptor to the wrong pipe. */
static int pipe_above_stdio(int *fds)
{
  int i;

  if (pipe(fds) < 0)
    return -1;

  for (i = 0; i < 2; i++)
    {
      if (fds[i] <= 2)
	{
	  int newfd = fcntl(fds[i], F_DUPFD, 3);

	  if (newfd < 0)
	    {
	      int err = errno;

	      close(fds[0]);
	      close(fds[1]);
	      errno = err;
	      return -1;
	    }
	  close(fds[i]);
	  fds[i] = newfd;
	}
    }

  return 0;
}

#ifdef HAVE_POSIX_SPAWN
/* Send the effective target environment to the helper.
   Returns 0, or an errno. */
static int write_spawn_env(int fd, char * const *newEnviron)
{
  char * const *env = (newEnviron != NULL) ? newEnviron : environ;
  int header[2];
  size_t total;
  size_t len = 0;
  size_t off;
  char *buf;
  int count;
  int err = 0;
  int i;

  for (count = 0; env[count] != NULL; count++)
    len += strlen(env[count]) + 1;

  if (len > INT_MAX)
    return E2BIG;

  header[0] = CPPROC_SPAWN_MAGIC;
  header[1] = (int) len;

  total = sizeof(header) + len;
  buf = malloc(total);
  if (buf == NULL)
    return ENOMEM;

  memcpy(buf, header, sizeof(header));
  off = sizeof(header);
  for (i = 0; i < count; i++)
    {
      size_t n = strlen(env[i]) + 1;

      memcpy(buf + off, env[i], n);
      off += n;
    }

  for (off = 0; off < total; )
    {
      ssize_t n = write(fd, buf + off, total - off);

      if (n < 0)
	{
	  if (errno == EINTR)
	    continue;
	  err = errno;
	  break;
	}
      off += (size_t) n;
    }

  free(buf);

  return err;
}


/* Decimal needs ~2.4 chars per byte, plus NUL terminator */
#define FD_ARG_SIZE ((3 * sizeof(int)) + 1)

/* Spawn the helper without file actions or attributes, so that older
   glibc versions will use vfork() internally, and not fork(). Returns
   the helper pid, or -1 with errno set. */
static pid_t spawn_via_helper(char * const *commandLine,
			      char * const *newEnviron, int *local_fds,
			      int pipe_count, int *fail_fds,
			      const char *path, const char *wd, int argc)
{
  char * const empty_envp[] = { NULL };
  char fd_args[5][FD_ARG_SIZE];
  int parent_fds[5];
  int env_fds[2];
  char **helper_argv;
  pid_t pid = -1;
  int err;
  int i;

  helper_argv = malloc((8 + argc + 1) * sizeof(char *));
  if (helper_argv == NULL)
    {
      errno = ENOMEM;
      return -1;
    }

  if (pipe_above_stdio(env_fds) < 0)
    {
      err = errno;
      free(helper_argv);
      errno = err;
      return -1;
    }

  /* Mark the parent's ends of these pipes close-on-exec, so the helper
     inherits only their child ends. In particular, if the helper also
     inherited the env pipe's write end, it would never see EOF if the
     parent dies mid-spawn, leaving the orphaned helper blocked forever
     (JDK-8307990). */
  parent_fds[0] = env_fds[1];
  parent_fds[1] = fail_fds[0];
  parent_fds[2] = local_fds[1];
  parent_fds[3] = local_fds[2];
  parent_fds[4] = (pipe_count == 3) ? local_fds[4] : -1;
  for (i = 0; i < 5; i++)
    {
      if (parent_fds[i] != -1
	  && fcntl(parent_fds[i], F_SETFD, FD_CLOEXEC) < 0)
	{
	  err = errno;
	  free(helper_argv);
	  close(env_fds[0]);
	  close(env_fds[1]);
	  errno = err;
	  return -1;
	}
    }

  snprintf(fd_args[0], FD_ARG_SIZE, "%d", local_fds[0]);
  snprintf(fd_args[1], FD_ARG_SIZE, "%d", local_fds[3]);
  snprintf(fd_args[2], FD_ARG_SIZE, "%d",
	   pipe_count == 3 ? local_fds[5] : local_fds[3]);
  snprintf(fd_args[3], FD_ARG_SIZE, "%d", fail_fds[1]);
  snprintf(fd_args[4], FD_ARG_SIZE, "%d", env_fds[0]);

  helper_argv[0] = (char *) CPPROC_SPAWN_HELPER;
  for (i = 0; i < 5; i++)
    helper_argv[1 + i] = fd_args[i];
  helper_argv[6] = (char *) (wd != NULL ? wd : ".");
  helper_argv[7] = (char *) path;
  for (i = 0; i < argc; i++)
    helper_argv[8 + i] = commandLine[i];
  helper_argv[8 + argc] = NULL;

  /* The helper is launched with an empty environment, so it must be
     loadable without environment-dependent search paths. */
  err = posix_spawn(&pid, CPPROC_SPAWN_HELPER, NULL, NULL, helper_argv, empty_envp);
  free(helper_argv);
  close(env_fds[0]);

  if (err == 0)
    err = write_spawn_env(env_fds[1], newEnviron);
  close(env_fds[1]);

  if (err != 0)
    {
      if (pid > 0)
	{
	  /* Helper was spawned, but the environment transfer failed.
	     We already closed the pipe's write end, so the helper's
	     read returns EOF and it exits. Reap it. */
	  int status;

	  while (waitpid(pid, &status, 0) < 0 && errno == EINTR)
	    ;
	}
      errno = err;
      return -1;
    }

  return pid;
}
#endif /* HAVE_POSIX_SPAWN */

int cpproc_forkAndExec (char * const *commandLine, char * const * newEnviron,
			int *fds, int pipe_count, pid_t *out_pid, const char *wd,
			int use_posix_spawn)
{
  int local_fds[6];
  int fail_fds[2];
  const char *path;
  sigset_t allsigs;
  sigset_t savedmask;
  int errnum;
  ssize_t n;
  int argc;
  int err;
  int i;
  pid_t pid = -1;

  /* Initialize the output fds so that the caller sees no garbage in
     them if we return with an error, or in the unused stderr entry
     when redirection is requested */
  for (i = 0; i < CPIO_EXEC_NUM_PIPES; i++)
    fds[i] = -1;

  path = getenv("PATH");
  if (path == NULL)
    path = "/bin:/usr/bin";
  for (argc = 0; commandLine[argc] != NULL; argc++)
    ;

  for (i = 0; i < (pipe_count * 2); i += 2)
    {
      if (pipe_above_stdio(&local_fds[i]) < 0)
	{
	  err = errno;
	  close_fds(local_fds, i);

	  return err;
	}
    }

  /* Extra pipe used by the child to report failure to the parent.
     On success the exec closes the write end (FD_CLOEXEC) and the
     parent reads EOF. */
  if (pipe_above_stdio(fail_fds) < 0)
    {
      err = errno;
      close_fds(local_fds, pipe_count * 2);

      return err;
    }

  /* Block all signals before we fork() to ensure that the child's
     setup is not interrupted, so no call can fail with EINTR. The
     mask also crosses posix_spawn's exec into the helper, whose
     setup runs equally shielded. */
  sigfillset(&allsigs);
  pthread_sigmask(SIG_SETMASK, &allsigs, &savedmask);

  if (use_posix_spawn)
    {
#ifdef HAVE_POSIX_SPAWN
      pid = spawn_via_helper(commandLine, newEnviron, local_fds,
			     pipe_count, fail_fds, path, wd, argc);
      err = errno;
#else
      err = ENOSYS;
#endif
    }
  else
    {
      /* Preallocate cp_execvpe's buffer because malloc is unsafe after fork.
	 The child gets its own copy, so the parent can free it immediately. */
      char **sh_argv = malloc((argc + 2) * sizeof(char *));

      if (sh_argv == NULL)
	err = ENOMEM;
      else
	{
	  pid = cpproc_child_fork_exec(commandLine, newEnviron, local_fds,
				       pipe_count, fail_fds, path, sh_argv,
				       wd, get_max_fd());
	  err = errno;
	  free(sh_argv);
	}
    }

  pthread_sigmask(SIG_SETMASK, &savedmask, NULL);

  if (pid == -1)
    {
      close_fds(local_fds, pipe_count * 2);
      close(fail_fds[0]);
      close(fail_fds[1]);
      return err;
    }
  close(fail_fds[1]);

  /* Wait for the outcome of the exec: EOF if it succeeded, the
     child's errno if not */
  do
    {
      n = read(fail_fds[0], &errnum, sizeof(errnum));
    }
  while (n < 0 && errno == EINTR);
  close(fail_fds[0]);

  if (n != 0)
    {
      int status;

      if (n != (ssize_t) sizeof(errnum))
	errnum = EIO;

      /* The child exited without exec'ing; reap it */
      while (waitpid(pid, &status, 0) < 0 && errno == EINTR)
	;

      close_fds(local_fds, pipe_count * 2);
      return errnum;
    }

  close(local_fds[0]);
  close(local_fds[3]);
  if (pipe_count == 3)
    close(local_fds[5]);

  fds[0] = local_fds[1];
  fds[1] = local_fds[2];
  if (pipe_count == 3)
    fds[2] = local_fds[4];
  *out_pid = pid;

  return 0;
}

int cpproc_waitpid (pid_t pid, int *status, pid_t *outpid, int options)
{
  pid_t wp = waitpid(pid, status, options);

  if (wp < 0)
    return errno;

  *outpid = wp;
  return 0;
}

int cpproc_kill (pid_t pid, int signal)
{
  if (kill(pid, signal) < 0)
    return errno;

  return 0;
}
