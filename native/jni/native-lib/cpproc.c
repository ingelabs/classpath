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
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <fcntl.h>
#ifdef HAVE_PTHREAD_H
#include <pthread.h>
#endif

#ifdef HAVE_VFORK
extern char **environ;
#endif

static void close_all_fds(int *fds, int numFds)
{
  int i;

  for (i = 0; i < numFds; i++)
    close(fds[i]);
}

static int should_use_vfork(void)
{
  const char *env = getenv("CLASSPATH_USE_VFORK");
#ifdef HAVE_VFORK
  if (env != NULL && env[0] == '0')
    return 0;
#ifdef HAVE_PTHREAD_SIGMASK
  return 1;
#else
  return 0;
#endif
#else
  (void) env;
  return 0;
#endif
}

static int find_spawnhelper(char *buffer, size_t buffer_len)
{
  const char *override = getenv("CLASSPATH_SPAWNHELPER");
  const char *candidate = NULL;

  if (override != NULL && override[0] != '\0')
    candidate = override;
#ifdef CP_SPAWNHELPER_PATH
  else
    candidate = CP_SPAWNHELPER_PATH;
#endif

  if (candidate == NULL || candidate[0] == '\0')
    return 0;

  if (strchr(candidate, '/') != NULL)
    {
      if (access(candidate, X_OK) != 0)
	return 0;
      if (strlen(candidate) + 1 > buffer_len)
	return 0;
      strcpy(buffer, candidate);
      return 1;
    }

  {
    const char *path = getenv("PATH");
    const char *start = path;

    if (path == NULL || path[0] == '\0')
      return 0;

    while (start != NULL && *start != '\0')
      {
	const char *end = strchr(start, ':');
	size_t dir_len = (end != NULL) ? (size_t)(end - start) : strlen(start);
	size_t needed = dir_len + 1 + strlen(candidate) + 1;

	if (dir_len > 0 && needed <= buffer_len)
	  {
	    memcpy(buffer, start, dir_len);
	    buffer[dir_len] = '/';
	    strcpy(buffer + dir_len + 1, candidate);
	    if (access(buffer, X_OK) == 0)
	      return 1;
	  }

	if (end == NULL)
	  break;
	start = end + 1;
      }
  }

  return 0;
}

#ifdef HAVE_VFORK
static pid_t vfork_exec_helper(const char *path, char *const *argv,
			       char *const *envp, int *err_out)
{
  pid_t pid = vfork();

  if (pid == 0)
    {
      execve(path, argv, envp);
      _exit(127);
    }

  if (pid < 0 && err_out != NULL)
    *err_out = errno;

  return pid;
}
#endif

static int read_exec_status(int fd, int *err_out)
{
  unsigned char *buf = (unsigned char *) err_out;
  size_t total = 0;

  while (total < sizeof(*err_out))
    {
      ssize_t count = read(fd, buf + total, sizeof(*err_out) - total);
      if (count == 0)
	return 0;
      if (count < 0)
	{
	  if (errno == EINTR)
	    continue;
	  return -1;
	}
      total += (size_t) count;
    }

  return 1;
}

int cpproc_forkAndExec (char * const *commandLine, char * const * newEnviron,
			int *fds, int pipe_count, pid_t *out_pid, const char *wd)
{
  int local_fds[6];
  int i;
  pid_t pid;
  int use_helper = 0;
  char helper_path[512];
  int exec_status_pipe[2] = { -1, -1 };

  for (i = 0; i < 6; i++)
    local_fds[i] = -1;

  for (i = 0; i < (pipe_count * 2); i += 2)
    {
      if (pipe(&local_fds[i]) < 0)
	{
	  int err = errno;

	  close_all_fds(local_fds, i);
	  
	  return err;
	}
    }

#ifdef HAVE_VFORK
  if (should_use_vfork() && find_spawnhelper(helper_path, sizeof(helper_path)))
    use_helper = 1;
#endif

  if (use_helper)
    {
      int cmd_count = 0;
      int err = 0;
      char **helper_argv = NULL;
      char pipe_buf[16];
      char redirect_buf[16];
      char fd_buf[6][16];
      char status_fd_buf[16];
      char *cwd_arg = (wd != NULL) ? (char *) wd : (char *) "-";
      char *const *envp = (newEnviron != NULL ? newEnviron : environ);
      int exec_status = 0;
#ifdef HAVE_PTHREAD_SIGMASK
      sigset_t oldmask;
      int have_oldmask = 0;
#endif
#ifdef HAVE_PTHREAD_SETCANCELSTATE
      int old_cancel_state = 0;
      int have_cancel_state = 0;
#endif

      while (commandLine[cmd_count] != NULL)
	cmd_count++;

      snprintf(pipe_buf, sizeof(pipe_buf), "%d", pipe_count);
      snprintf(redirect_buf, sizeof(redirect_buf), "%d", (int) (pipe_count == 2));
      for (i = 0; i < 6; i++)
	snprintf(fd_buf[i], sizeof(fd_buf[i]), "%d", local_fds[i]);

      if (pipe(exec_status_pipe) < 0)
	{
	  close_all_fds(local_fds, pipe_count * 2);
	  return errno;
	}
      if (fcntl(exec_status_pipe[1], F_SETFD, FD_CLOEXEC) < 0)
	{
	  err = errno;
	  close(exec_status_pipe[0]);
	  close(exec_status_pipe[1]);
	  close_all_fds(local_fds, pipe_count * 2);
	  return err;
	}

      snprintf(status_fd_buf, sizeof(status_fd_buf), "%d", exec_status_pipe[1]);

      helper_argv = malloc(sizeof(*helper_argv) * (14 + cmd_count));
      if (helper_argv == NULL)
	{
	  close(exec_status_pipe[0]);
	  close(exec_status_pipe[1]);
	  close_all_fds(local_fds, pipe_count * 2);
	  return ENOMEM;
	}

      helper_argv[0] = helper_path;
      helper_argv[1] = pipe_buf;
      helper_argv[2] = redirect_buf;
      for (i = 0; i < 6; i++)
	helper_argv[3 + i] = fd_buf[i];
      helper_argv[9] = status_fd_buf;
      helper_argv[10] = cwd_arg;
      helper_argv[11] = (char *) "--";
      for (i = 0; i < cmd_count; i++)
	helper_argv[12 + i] = commandLine[i];
      helper_argv[12 + cmd_count] = NULL;

#ifdef HAVE_PTHREAD_SIGMASK
      if (sigfillset(&oldmask) == 0)
	{
	  sigset_t blockmask;
	  blockmask = oldmask;
	  if (pthread_sigmask(SIG_SETMASK, &blockmask, &oldmask) == 0)
	    have_oldmask = 1;
	}
#endif

#ifdef HAVE_PTHREAD_SETCANCELSTATE
      if (pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, &old_cancel_state) == 0)
	have_cancel_state = 1;
#endif

      pid = vfork_exec_helper(helper_path, helper_argv, envp, &err);

#ifdef HAVE_PTHREAD_SETCANCELSTATE
      if (have_cancel_state)
	pthread_setcancelstate(old_cancel_state, NULL);
#endif

#ifdef HAVE_PTHREAD_SIGMASK
      if (have_oldmask)
	pthread_sigmask(SIG_SETMASK, &oldmask, NULL);
#endif

      free(helper_argv);
      if (pid < 0)
	{
	  close(exec_status_pipe[0]);
	  close(exec_status_pipe[1]);
	  close_all_fds(local_fds, pipe_count * 2);
	  return err;
	}

      close(exec_status_pipe[1]);
      exec_status = read_exec_status(exec_status_pipe[0], &err);
      close(exec_status_pipe[0]);
      if (exec_status != 0)
	{
	  if (exec_status < 0)
	    err = errno;
	  close_all_fds(local_fds, pipe_count * 2);
	  waitpid(pid, NULL, 0);
	  return err != 0 ? err : EIO;
	}

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
