/* cpproc.c -
   Copyright (C) 2003, 2004, 2005, 2006, 2025  Free Software Foundation, Inc.

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
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>

/* Helper binary path - defined by configure */
#ifndef CPPROCHELPER_PATH
#define CPPROCHELPER_PATH "/usr/libexec/cpprochelper"
#endif

/* Number of fixed arguments for the helper before command args */
#define HELPER_FIXED_ARGS 6

/* Buffer size for integer-to-string conversion */
#define INT_STR_SIZE 12

static void close_all_fds(int *fds, int numFds)
{
  int i;

  for (i = 0; i < numFds; i++)
    close(fds[i]);
}

/*
 * Count the number of elements in a NULL-terminated string array.
 */
static int count_args(char * const *args)
{
  int count = 0;
  while (args[count] != NULL)
    count++;
  return count;
}

/*
 * Read exactly 'count' bytes from fd, handling EINTR.
 * Returns number of bytes read, or -1 on error.
 */
static ssize_t read_all(int fd, void *buf, size_t count)
{
  char *p = (char *)buf;
  size_t remaining = count;

  while (remaining > 0)
    {
      ssize_t n = read(fd, p, remaining);
      if (n < 0)
        {
          if (errno == EINTR)
            continue;
          return -1;
        }
      if (n == 0)
        break;  /* EOF */
      p += n;
      remaining -= n;
    }
  return count - remaining;
}

int cpproc_forkAndExec (char * const *commandLine, char * const * newEnviron,
			int *fds, int pipe_count, pid_t *out_pid, const char *wd,
			int use_vfork)
{
  int local_fds[6];
  int status_pipe[2];
  int i;
  pid_t pid;
  int cmd_argc;
  int helper_argc;
  char **helper_argv;
  char status_fd_str[INT_STR_SIZE];
  char stdin_fd_str[INT_STR_SIZE];
  char stdout_fd_str[INT_STR_SIZE];
  char stderr_fd_str[INT_STR_SIZE];
  int child_errno;
  ssize_t n;
  int err;
  int stdin_child_fd, stdout_child_fd, stderr_child_fd;

  /* Create pipes for stdin/stdout/stderr */
  for (i = 0; i < (pipe_count * 2); i += 2)
    {
      if (pipe(&local_fds[i]) < 0)
        {
          err = errno;
          close_all_fds(local_fds, i);
          return err;
        }
    }

  /* Create status pipe for error reporting from helper */
  if (pipe(status_pipe) < 0)
    {
      err = errno;
      close_all_fds(local_fds, pipe_count * 2);
      return err;
    }

  /*
   * Build helper argv array (before vfork, while we can safely allocate memory)
   *
   * Helper arguments:
   *   argv[0]: helper path
   *   argv[1]: status_fd
   *   argv[2]: stdin_fd
   *   argv[3]: stdout_fd
   *   argv[4]: stderr_fd
   *   argv[5]: cwd (empty string if not changing directory)
   *   argv[6]: cmd
   *   argv[7...]: cmd args
   *   argv[N]: NULL
   */

  cmd_argc = count_args(commandLine);
  helper_argc = HELPER_FIXED_ARGS + cmd_argc + 1;  /* +1 for NULL terminator */

  helper_argv = (char **)malloc(helper_argc * sizeof(char *));
  if (helper_argv == NULL)
    {
      err = errno;
      close_all_fds(local_fds, pipe_count * 2);
      close(status_pipe[0]);
      close(status_pipe[1]);
      return err;
    }

  /* Determine child's FDs for stdin/stdout/stderr */
  stdin_child_fd = local_fds[0];     /* Read end of stdin pipe */
  stdout_child_fd = local_fds[3];    /* Write end of stdout pipe */
  if (pipe_count == 3)
    stderr_child_fd = local_fds[5];  /* Write end of stderr pipe */
  else
    stderr_child_fd = local_fds[3];  /* Merge stderr with stdout */

  /* Convert FD numbers to strings */
  snprintf(status_fd_str, INT_STR_SIZE, "%d", status_pipe[1]);
  snprintf(stdin_fd_str, INT_STR_SIZE, "%d", stdin_child_fd);
  snprintf(stdout_fd_str, INT_STR_SIZE, "%d", stdout_child_fd);
  snprintf(stderr_fd_str, INT_STR_SIZE, "%d", stderr_child_fd);

  /* Build helper argument vector */
  helper_argv[0] = (char *)CPPROCHELPER_PATH;
  helper_argv[1] = status_fd_str;
  helper_argv[2] = stdin_fd_str;
  helper_argv[3] = stdout_fd_str;
  helper_argv[4] = stderr_fd_str;
  helper_argv[5] = (wd != NULL && wd[0] != '\0') ? (char *)wd : (char *)"";

  /* Copy command and its arguments */
  for (i = 0; i < cmd_argc; i++)
    helper_argv[HELPER_FIXED_ARGS + i] = commandLine[i];
  helper_argv[HELPER_FIXED_ARGS + cmd_argc] = NULL;

  /*
   * Fork (or vfork) and exec the helper.
   *
   * With vfork, the child shares the parent's memory until exec, so we must:
   * 1. Block all signals to prevent parent's signal handlers running in child
   * 2. Disable pthread cancellation to prevent cleanup handlers running
   * 3. Immediately exec without modifying variables or calling unsafe functions
   */
#ifdef HAVE_VFORK
  if (use_vfork)
    {
      sigset_t all_signals, old_sigmask;
      int old_cancel_state;

      /* Block all signals */
      sigfillset(&all_signals);
      pthread_sigmask(SIG_BLOCK, &all_signals, &old_sigmask);

      /* Disable pthread cancellation */
      pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, &old_cancel_state);

      pid = vfork();

      if (pid == 0)
        {
          /* Child: immediately exec helper, passing environment via execve */
          if (newEnviron != NULL)
            execve(CPPROCHELPER_PATH, helper_argv, newEnviron);
          else
            execv(CPPROCHELPER_PATH, helper_argv);

          /* exec failed - exit with error code */
          _exit(127);
        }

      /* Parent: restore signal mask and cancellation state */
      pthread_setcancelstate(old_cancel_state, NULL);
      pthread_sigmask(SIG_SETMASK, &old_sigmask, NULL);
    }
  else
#endif
    {
      pid = fork();

      if (pid == 0)
        {
          /* Child: immediately exec helper, passing environment via execve */
          if (newEnviron != NULL)
            execve(CPPROCHELPER_PATH, helper_argv, newEnviron);
          else
            execv(CPPROCHELPER_PATH, helper_argv);

          /* exec failed - exit with error code */
          _exit(127);
        }
    }

  /* Parent continues here */

  /* Free helper_argv - no longer needed */
  free(helper_argv);

  if (pid < 0)
    {
      /* Fork failed */
      err = errno;
      close_all_fds(local_fds, pipe_count * 2);
      close(status_pipe[0]);
      close(status_pipe[1]);
      return err;
    }

  /* Close child's ends of the pipes */
  close(local_fds[0]);   /* stdin read end */
  close(local_fds[3]);   /* stdout write end */
  if (pipe_count == 3)
    close(local_fds[5]); /* stderr write end */

  /* Close write end of status pipe (child writes, parent reads) */
  close(status_pipe[1]);

  /*
   * Read from status pipe to check for errors.
   *
   * Protocol:
   * - If exec succeeds, the pipe closes (CLOEXEC) and we read 0 bytes
   * - If exec fails, helper writes errno (4 bytes) before exiting
   */
  n = read_all(status_pipe[0], &child_errno, sizeof(child_errno));
  close(status_pipe[0]);

  if (n == 0)
    {
      /* Success - exec closed pipe via CLOEXEC */
      fds[0] = local_fds[1];  /* stdin write end (parent writes to child) */
      fds[1] = local_fds[2];  /* stdout read end (parent reads from child) */
      fds[2] = local_fds[4];  /* stderr read end (parent reads from child) */
      *out_pid = pid;
      return 0;
    }
  else if (n == sizeof(child_errno))
    {
      /* Failure - helper reported an error */
      /* Close parent's ends of the pipes */
      close(local_fds[1]);
      close(local_fds[2]);
      close(local_fds[4]);

      /* Wait for child to exit to avoid zombies */
      waitpid(pid, NULL, 0);

      return child_errno;
    }
  else
    {
      /* Unexpected - partial read or error reading status pipe */
      close(local_fds[1]);
      close(local_fds[2]);
      close(local_fds[4]);

      waitpid(pid, NULL, 0);

      /* Return a generic error */
      return (n < 0) ? errno : ECHILD;
    }
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
