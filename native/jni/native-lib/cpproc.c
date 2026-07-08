/* cpproc.c -
   Copyright (C) 2003, 2004, 2005, 2006  Free Software Foundation, Inc.

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
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <errno.h>
#include <fcntl.h>
#include <limits.h>
#include <stdlib.h>
#include <string.h>

/* PATH_MAX is not guaranteed to be defined (e.g. on GNU Hurd) */
#ifndef PATH_MAX
#define PATH_MAX 4096
#endif

static void close_all_fds(int *fds, int numFds)
{
  int i;

  for (i = 0; i < numFds; i++)
    close(fds[i]);
}

/* Like execve, but also implementing execvp's "shell fallback"
   behaviour: if execve fails with ENOEXEC, try to execute as a
   script via /bin/sh. The shell receives the script path (file)
   followed by the original arguments minus argv[0], which is
   dropped. If envp is NULL the environment is inherited (execv is
   used instead of execve). */
static void cp_execve_sh(const char *file, char * const *argv,
			 char * const *envp, char **sh_argv)
{
  if (envp != NULL)
    execve(file, argv, envp);
  else
    execv(file, argv);

  if (errno == ENOEXEC)
    {
      int i;

      sh_argv[0] = (char *) "/bin/sh";
      sh_argv[1] = (char *) file;
      for (i = 1; argv[i] != NULL; i++)
	sh_argv[i + 1] = argv[i];
      sh_argv[i + 1] = NULL;

      if (envp != NULL)
	execve("/bin/sh", sh_argv, envp);
      else
	execv("/bin/sh", sh_argv);
    }
}

/* Replacement for execvpe, which is a GNU extension and not available
   everywhere. If envp is NULL the environment is inherited. The
   supplied preallocated sh_argv array must have room for one entry
   more than argv, including its terminating NULL. */
static void cp_execvpe(const char *file, char * const *argv,
		       char * const *envp, const char *path,
		       char **sh_argv)
{
  /* - This runs in the child of a fork of a multi-threaded process,
       so it may only execute async-signal-safe operations.
     - If execve fails with ENOEXEC, we assume it is a script with +x
       permission (otherwise we would have seen EACCES) but without a
       shebang line, and execute it via /bin/sh, as execvp would do.
       The fallback is implemented explicitly because execve does not
       provide it, and execvp (which does) is not async-signal-safe.
     - OpenJDK implements a similar execvpe replacement, except that
       they do use execvp in fork mode (see childproc.c). */
  char buffer[PATH_MAX];
  const char *p, *next;
  size_t filelen = strlen(file);
  int got_eacces = 0;

  /* An empty command name fails with ENOENT */
  if (*file == '\0')
    {
      errno = ENOENT;
      return;
    }

  /* Command names containing a slash are not looked up in the PATH */
  if (strchr(file, '/') != NULL)
    {
      cp_execve_sh(file, argv, envp, sh_argv);
      return;
    }

  for (p = path; p != NULL; p = next)
    {
      const char *candidate;
      const char *sep;
      size_t len;

      sep = strchr(p, ':');
      next = (sep != NULL) ? sep + 1 : NULL;
      len = (sep != NULL) ? (size_t) (sep - p) : strlen(p);
      if (len == 0)
	{
	  /* An empty PATH element means the current directory */
	  candidate = file;
	}
      else if (len + filelen + 2 <= sizeof(buffer))
	{
	  memcpy(buffer, p, len);
	  buffer[len] = '/';
	  strcpy(buffer + len + 1, file);
	  candidate = buffer;
	}
      else
	{
	  errno = ENAMETOOLONG;
	  continue;
	}

      cp_execve_sh(candidate, argv, envp, sh_argv);
      switch (errno)
	{
	case EACCES:
	  /* Keep searching, but report EACCES if nothing is found */
	  got_eacces = 1;
	  break;
	case ENOENT:
	case ENOTDIR:
#ifdef ELOOP
	case ELOOP:
#endif
#ifdef ESTALE
	case ESTALE:
#endif
#ifdef ENODEV
	case ENODEV:
#endif
#ifdef ETIMEDOUT
	case ETIMEDOUT:
#endif
	  break;
	default:
	  return;
	}
    }

  if (got_eacces)
    errno = EACCES;
}

int cpproc_forkAndExec (char * const *commandLine, char * const * newEnviron,
			int *fds, int pipe_count, pid_t *out_pid, const char *wd)
{
  int local_fds[6];
  int fail_fds[2];
  const char *path;
  char **sh_argv;
  int errnum;
  ssize_t n;
  int argc;
  int i;
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

  for (i = 0; i < (pipe_count * 2); i += 2)
    {
      if (pipe(&local_fds[i]) < 0)
	{
	  int err = errno;

	  close_all_fds(local_fds, i);
	  free(sh_argv);

	  return err;
	}
    }

  /* Extra pipe used by the child to report a failed chdir or exec to
     the parent. On success the exec closes the write end (FD_CLOEXEC)
     and the parent reads EOF. */
  if (pipe(fail_fds) < 0)
    {
      int err = errno;

      close_all_fds(local_fds, pipe_count * 2);
      free(sh_argv);

      return err;
    }

  pid = fork();

  switch (pid)
    {
    case 0:
      close(fail_fds[0]);
      if (fcntl(fail_fds[1], F_SETFD, FD_CLOEXEC) < 0)
	goto child_error;

      dup2(local_fds[0], 0);
      dup2(local_fds[3], 1);
      if (pipe_count == 3)
	dup2(local_fds[5], 2);
      else
	dup2(1, 2);

      close_all_fds(local_fds, pipe_count * 2);

      if (wd == NULL || chdir(wd) == 0)
	cp_execvpe(commandLine[0], commandLine, newEnviron, path, sh_argv);

    child_error:
      /* The fcntl, chdir or exec failed; send our errno to the parent */
      errnum = errno;
      while (write(fail_fds[1], &errnum, sizeof(errnum)) < 0
	     && errno == EINTR)
	;
      _exit(127);

    case -1:
      {
	int err = errno;

	close_all_fds(local_fds, pipe_count * 2);
	close(fail_fds[0]);
	close(fail_fds[1]);
	free(sh_argv);
	return err;
      }
    default:
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

	  close_all_fds(local_fds, pipe_count * 2);
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

  /* keep compiler happy */

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
