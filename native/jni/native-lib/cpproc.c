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
#include <stdlib.h>

static void close_all_fds(int *fds, int numFds)
{
  int i;

  for (i = 0; i < numFds; i++)
    close(fds[i]);
}

int cpproc_forkAndExec (char * const *commandLine, char * const * newEnviron,
			int *fds, int pipe_count, pid_t *out_pid, const char *wd)
{
  int local_fds[6];
  int fail_fds[2];
  int errnum;
  ssize_t n;
  int i;
  pid_t pid;

  /* Initialize the output fds so that the caller sees no garbage in
     them if we return with an error, or in the unused stderr entry
     when redirection is requested */
  for (i = 0; i < CPIO_EXEC_NUM_PIPES; i++)
    fds[i] = -1;

  for (i = 0; i < (pipe_count * 2); i += 2)
    {
      if (pipe(&local_fds[i]) < 0)
	{
	  int err = errno;

	  close_all_fds(local_fds, i);

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
	{
	  if (newEnviron == NULL)
	    execvp(commandLine[0], commandLine);
	  else
	    execve(commandLine[0], commandLine, newEnviron);
	}

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
	return err;
      }
    default:
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
