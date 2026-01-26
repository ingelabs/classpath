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
#include <pthread.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <errno.h>
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
  int i;
  pid_t pid;

  for (i = 0; i < (pipe_count * 2); i += 2)
    {
      if (pipe(&local_fds[i]) < 0)
	{
	  int err = errno;

	  close_all_fds(local_fds, i);
	  
	  return err;
	}
    }
  
  pid = fork();
  
  switch (pid)
    {
    case 0:
      dup2(local_fds[0], 0);
      dup2(local_fds[3], 1);
      if (pipe_count == 3)
	dup2(local_fds[5], 2);
      else
	dup2(1, 2);

      close_all_fds(local_fds, pipe_count * 2);

      i = chdir(wd);
      /* FIXME: Handle the return value */
      if (newEnviron == NULL)
	execvp(commandLine[0], commandLine);
      else
	execve(commandLine[0], commandLine, newEnviron);
      
      abort();
      
      break;
    case -1:
      {
	int err = errno;
	
	close_all_fds(local_fds, pipe_count * 2);
	return err;
      }
    default: 
      close(local_fds[0]);
      close(local_fds[3]);
      if (pipe_count == 3)
	close(local_fds[5]);

      fds[0] = local_fds[1];
      fds[1] = local_fds[2];
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


/* vfork support
 * ----------------------------------------------------- */

#ifndef VFORK_HELPER
#error  Path to vfork helper not defined
#endif

/* This is defined by posix_spawn */
#define EXIT_ERROR 127

#define RESTARTABLE_IO(_cmd, _ret) \
    { \
        do { \
            _ret = _cmd; \
        } while ((_ret == -1) && (errno == EINTR)); \
    }

#define SAFE_CLOSE_PIPE(_fds) \
    { \
        if (_fds[0] != -1) { close(_fds[0]); } \
        if (_fds[1] != -1) { close(_fds[1]); } \
    }


extern char ** environ;

static char ** createPrependedArgv(const char * path, const char * chdir,
                                   char * const * argv, int length, int * fds);
static void freePargv(char ** pargv);


/* Actually spawn the child process by calling vfork + exec. This function
 * only returns in the parent.
 */
static pid_t spawn_child(char * const * pargv, char * const * env, int status_fd)
{
    pid_t pid;
    char res;
    int i;
    int oldcs;
    sigset_t newmask, oldmask;

    /* Block all signals while we vfork+exec to avoid parent signal
     * handlers being called in the child. */

    sigfillset(&newmask);
    pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, &oldcs);
    pthread_sigmask(SIG_SETMASK, &newmask, &oldmask);

    pid = vfork();

    if (pid == 0)
    {
        /* Child */
        execve(pargv[0], pargv, env);

        /* If we got here, then that means execve failed. Try to notify the
         * parent, although we are not supposed to call anything other than
         * execve or _exit after vfork. But if we got this far, something
         * has gone very wrong already... */

        res = (errno <= 255)? errno : 255;
        RESTARTABLE_IO( write(status_fd, &res, 1), i );

        _exit(EXIT_ERROR);
    }

    pthread_sigmask(SIG_SETMASK, &oldmask, NULL);
    pthread_setcancelstate(oldcs, NULL);

    /* Parent */
    return pid;
}

int cpproc_vforkAndExec (char * const *commandLine, char * const * newEnviron,
			 int *fds, int pipe_count, pid_t *out_pid, const char *wd)
{
    char **pargv;
    int in[2], out[2], err[2], status[2];
    int child_fds[4];
    int have_stderr = (pipe_count == 3);
    int i;
    int retval;
    char res;
    pid_t pid;

    in[0] = in[1] = out[0] = out[1] = err[0] = err[1] = status[0] = status[1] = -1;

    if ((pipe(in) < 0)
            || (pipe(out) < 0)
            || (have_stderr && (pipe(err) < 0))
            || (pipe(status) < 0))
    {
        retval = errno;
        goto err_exit;
    }

    child_fds[0] = in[0];
    child_fds[1] = out[1];
    child_fds[2] = have_stderr ? err[1] : out[1];
    child_fds[3] = status[1];

    pargv = createPrependedArgv(VFORK_HELPER, wd, commandLine, 0, child_fds);
    if (!pargv)
    {
        retval = ENOMEM;
        goto err_exit;
    }

    pid = spawn_child(pargv, (newEnviron != NULL)? newEnviron : environ, status[1]);
    freePargv(pargv);

    if (pid == -1)
    {
        retval = errno;
        goto err_exit;
    }

    /* On success, the child will close their end of the status pipe; this
     * makes the read() call below return 0 (EOF). For this to work we must
     * make sure to close the same fd in the parent process before calling
     * read(); otherwise, read() would block forever (pipe still open, but
     * nobody writing to it). */

    close(status[1]);
    status[1] = -1;

    RESTARTABLE_IO( read(status[0], &res, 1), i );
    if (i != 0)
    {
        if (i == -1)
        {
            retval = errno;
        }
        else
        {
            /* If the original errno didn't fit in one byte, make something up */
            retval = (res == 0xff)? ENOEXEC : (0xff & ((int) res));
        }

        waitpid(pid, NULL, 0);

        goto err_exit;
    }

    close(in[0]);
    close(out[1]);
    close(status[0]);
    if (have_stderr)
        close(err[1]);

    fds[0] = in[1];
    fds[1] = out[0];
    fds[2] = err[0];   	/* Ignored by caller if !have_stderr */
    *out_pid = pid;

    return 0;

err_exit:
    SAFE_CLOSE_PIPE(in);
    SAFE_CLOSE_PIPE(out);
    SAFE_CLOSE_PIPE(err);
    SAFE_CLOSE_PIPE(status);

    return retval;
}

/* ASCII needs 2.4 times more space, plus NUL terminator */
#define FD_ARG_SIZE ((3 * sizeof(int)) + 1)

static char ** createPrependedArgv(const char * path, const char * chdir,
                                   char * const * argv, int length, int * fds)
{
    char ** pargv;
    char * buf;
    int i;

    if (!length) {
        while (argv[length]) length++;
    }
    if (!chdir) {
    	chdir = ".";
    }

    /* Arg list */
    pargv = (char **) malloc(sizeof(char *) * (length + 7));
    if (pargv == NULL) {
        return NULL;
    }

    /* Char buffer for fd arguments */
    buf = (char *) malloc(4 * FD_ARG_SIZE);
    if (buf == NULL) {
	pargv[1] = NULL;
        goto error;
    }

    pargv[0] = (char *) path;
    pargv[5] = (char *) chdir;
    pargv[length + 6] = NULL;

    for (i = 0; i < 4; i++) {
        pargv[i + 1] = buf + (FD_ARG_SIZE * i);
        snprintf(pargv[i + 1], FD_ARG_SIZE, "%d", fds[i]);
    }

    for (i = 0; i < length; i++) {
        pargv[i + 6] = argv[i];
    }

    return pargv;

error:
    freePargv(pargv);
    return NULL;
}

static void freePargv(char ** pargv)
{
    if (pargv != NULL) {
        if (pargv[1] != NULL) {
            free(pargv[1]);
        }
        free(pargv);
    }
}

