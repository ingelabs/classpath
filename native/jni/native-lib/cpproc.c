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
#include "cpproc.h"
#include "cpproc-child.h"
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <errno.h>
#include <fcntl.h>
#include <limits.h>
#include <stdlib.h>
#include <string.h>

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

int cpproc_forkAndExec (char * const *commandLine, char * const * newEnviron,
			int *fds, int pipe_count, pid_t *out_pid, const char *wd)
{
  int local_fds[6];
  int fail_fds[2];
  const char *path;
  char **sh_argv;
  sigset_t allsigs;
  sigset_t savedmask;
  int errnum;
  ssize_t n;
  int argc;
  int i;
  int maxfd;
  pid_t pid;

  /* Initialize the output fds so that the caller sees no garbage in
     them if we return with an error, or in the unused stderr entry
     when redirection is requested */
  for (i = 0; i < CPIO_EXEC_NUM_PIPES; i++)
    fds[i] = -1;

  /* Preallocate the buffer used by cp_execvpe in the child: after the
     fork of a multi-threaded process only async-signal-safe operations
     may be executed, so no malloc there */
  path = getenv("PATH");
  if (path == NULL)
    path = "/bin:/usr/bin";
  for (argc = 0; commandLine[argc] != NULL; argc++)
    ;
  sh_argv = malloc((argc + 2) * sizeof(char *));
  if (sh_argv == NULL)
    return ENOMEM;

  maxfd = get_max_fd();

  for (i = 0; i < (pipe_count * 2); i += 2)
    {
      if (pipe_above_stdio(&local_fds[i]) < 0)
	{
	  int err = errno;

	  close_fds(local_fds, i);
	  free(sh_argv);

	  return err;
	}
    }

  /* Extra pipe used by the child to report failure to the parent.
     On success the exec closes the write end (FD_CLOEXEC) and the
     parent reads EOF. */
  if (pipe_above_stdio(fail_fds) < 0)
    {
      int err = errno;

      close_fds(local_fds, pipe_count * 2);
      free(sh_argv);

      return err;
    }

  /* Block all signals before we fork() to ensure that the child's
     setup is not interrupted, so no call can fail with EINTR. */
  sigfillset(&allsigs);
  pthread_sigmask(SIG_SETMASK, &allsigs, &savedmask);

  pid = cpproc_child_fork_exec(commandLine, newEnviron, local_fds,
			       pipe_count, fail_fds, path, sh_argv, wd,
			       maxfd);

  if (pid == -1)
    {
      int err = errno;

      pthread_sigmask(SIG_SETMASK, &savedmask, NULL);
      close_fds(local_fds, pipe_count * 2);
      close(fail_fds[0]);
      close(fail_fds[1]);
      free(sh_argv);
      return err;
    }

  pthread_sigmask(SIG_SETMASK, &savedmask, NULL);
  free(sh_argv);
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
