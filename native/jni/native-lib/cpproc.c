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

/* For close_range() */
#ifndef _GNU_SOURCE
#define _GNU_SOURCE 1
#endif

#include "config.h"
#include <jni.h>
#include "cpproc.h"
#include <dirent.h>
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

/* Bound the last-resort fcntl scan when OPEN_MAX is pathologically large. */
#define MAX_FD_SCAN 65536

static void close_fds(int *fds, int numFds);
static void child_process(char * const *commandLine,
			  char * const *newEnviron,
			  int *local_fds, int pipe_count, int *fail_fds,
			  const char *path, char **sh_argv, const char *wd,
			  int maxfd);

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

  pid = fork();

  switch (pid)
    {
    case 0:
      child_process(commandLine, newEnviron, local_fds, pipe_count,
		    fail_fds, path, sh_argv, wd, maxfd);
      /* child_process() does not return. */
      _exit(127);

    case -1:
      {
	int err = errno;

	pthread_sigmask(SIG_SETMASK, &savedmask, NULL);
	close_fds(local_fds, pipe_count * 2);
	close(fail_fds[0]);
	close(fail_fds[1]);
	free(sh_argv);
	return err;
      }
    default:
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


/* Child-side implementation.

   Everything below this point may run in the child of a fork() of a
   multi-threaded process, so it should only use async-signal-safe
   operations. Avoid malloc and functions that may take locks. Any
   buffers that require allocation must be preallocated by the parent
   and passed in. */

/* Also used by the parent. */
static void close_fds(int *fds, int numFds)
{
  int i;

  for (i = 0; i < numFds; i++)
    close(fds[i]);
}

static int mark_fd_cloexec(int fd)
{
  int flags = fcntl(fd, F_GETFD);

  if (flags < 0)
    return -1;

  if (!(flags & FD_CLOEXEC))
    return fcntl(fd, F_SETFD, flags | FD_CLOEXEC);

  return 0;
}

/* Walk the process' open fds directory to avoid scanning all possible
   fds up to OPEN_MAX. This uses opendir/readdir/closedir, which are
   not specified async-signal-safe, but should be safe in practice after
   fork (not vfork!) on Linux (glibc, musl >= 1.2.2) and macOS. OpenJDK
   uses the same approach, as does CPython in its non-Linux fallback.

   We deliberately do not use this on *BSD: without fdescfs mounted
   (not the default), /dev/fd is a static directory (0, 1, 2, or 0..63)
   and enumerating it would silently miss open descriptors. */
#if defined(__linux__)
#define FD_DIR "/proc/self/fd"
#elif defined(__APPLE__)
#define FD_DIR "/dev/fd"
#endif

#ifdef FD_DIR
static int mark_dir_fds_cloexec(void)
{
  DIR *dir;
  int result;

  dir = opendir(FD_DIR);
  if (dir == NULL)
    return -1;

  /* The directory stream's own fd may appear in FD_DIR. We don't
     want to close it while we walk the dir, but setting FD_CLOEXEC
     on it is harmless: it remains open until closedir(). */

  for (;;)
    {
      struct dirent *entry;
      char *name;
      int fd;

      errno = 0;
      entry = readdir(dir);
      if (entry == NULL)
	{
	  result = errno == 0 ? 0 : -1;
	  break;
	}

      name = entry->d_name;
      if (name[0] >= '0' && name[0] <= '9'
	  && (fd = strtol(name, NULL, 10)) >= 3
	  && mark_fd_cloexec(fd) < 0)
	{
	  result = -1;
	  break;
	}
    }

  closedir(dir);
  return result;
}
#endif

/* Mark every non-standard descriptor close-on-exec in the child after fork. */
static int mark_nonstd_fds_cloexec(int maxfd)
{
  int fd;

#if defined(HAVE_CLOSE_RANGE) && defined(CLOSE_RANGE_CLOEXEC)
  if (close_range(3, UINT_MAX, CLOSE_RANGE_CLOEXEC) == 0)
    return 0;
#endif

#ifdef FD_DIR
  if (mark_dir_fds_cloexec() == 0)
    return 0;
#endif

  for (fd = 3; fd < maxfd; fd++)
    if (mark_fd_cloexec(fd) < 0 && errno != EBADF)
      return -1;

  return 0;
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
  /* - If execve fails with ENOEXEC, we assume it is a script with +x
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

static void child_process(char * const *commandLine,
			  char * const *newEnviron,
			  int *local_fds, int pipe_count, int *fail_fds,
			  const char *path, char **sh_argv, const char *wd,
			  int maxfd)
{
  sigset_t sigmask;
  int errnum;

  close(fail_fds[0]);

  if (dup2(local_fds[0], 0) < 0)
    goto child_error;
  if (dup2(local_fds[3], 1) < 0)
    goto child_error;
  if (pipe_count == 3)
    {
      if (dup2(local_fds[5], 2) < 0)
	goto child_error;
    }
  else if (dup2(1, 2) < 0)
    goto child_error;

  close_fds(local_fds, pipe_count * 2);

  /* Mark non-standard fds (>= 3) close-on-exec. This includes fail_fds[1],
     which must stay open until exec(), and should be closed automatically
     if exec() succeeds. */
  if (mark_nonstd_fds_cloexec(maxfd) < 0)
    goto child_error;

  if (wd != NULL && chdir(wd) != 0)
    goto child_error;

  /* Reset the signal mask so that the executed program starts with all
     signals unblocked. */
  sigemptyset(&sigmask);
  if (sigprocmask(SIG_SETMASK, &sigmask, NULL) < 0)
    goto child_error;

  cp_execvpe(commandLine[0], commandLine, newEnviron, path, sh_argv);

 child_error:
  /* Child setup or exec itself failed; send our errno to the parent */
  errnum = errno;
  while (write(fail_fds[1], &errnum, sizeof(errnum)) < 0
	 && errno == EINTR)
    ;
  _exit(127);
}
