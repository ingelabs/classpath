/* cpprochelper.c - Helper binary for vfork-based process spawning
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

/*
 * Helper binary for vfork-based process spawning.
 *
 * After vfork(), the child shares the parent's memory, making it unsafe to:
 * - Iterate through FDs to close them
 * - Call chdir() (would change parent's cwd)
 * - Do complex setup before exec
 *
 * This helper is exec'd immediately after vfork() and runs in its own
 * address space where it can safely perform these operations.
 *
 * Usage:
 *   cpprochelper <status_fd> <stdin_fd> <stdout_fd> <stderr_fd> <cwd> <cmd> [args...]
 *
 * Arguments:
 *   argv[1]: status_fd - FD for reporting errors back to parent
 *   argv[2]: stdin_fd  - FD to redirect to stdin (0)
 *   argv[3]: stdout_fd - FD to redirect to stdout (1)
 *   argv[4]: stderr_fd - FD to redirect to stderr (2)
 *   argv[5]: cwd       - Working directory (empty string = no chdir)
 *   argv[6]: cmd       - Command to execute
 *   argv[7...]: args   - Command arguments
 *
 * Error reporting protocol:
 *   - Status pipe is marked CLOEXEC
 *   - On exec success: pipe closes automatically, parent reads 0 bytes
 *   - On failure: helper writes errno (4 bytes), parent reads it
 */

#include <errno.h>
#include <fcntl.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <dirent.h>
#include <sys/resource.h>

#define HELPER_ARGC_MIN 7

/*
 * Write errno to the status pipe and exit.
 * Uses a loop to handle partial writes (though unlikely for 4 bytes).
 */
static void report_error_and_exit(int status_fd, int err)
{
  ssize_t written = 0;
  ssize_t total = sizeof(err);
  char *buf = (char *)&err;

  while (written < total)
    {
      ssize_t n = write(status_fd, buf + written, total - written);
      if (n < 0)
        {
          if (errno == EINTR)
            continue;
          break;
        }
      written += n;
    }
  _exit(127);
}

/*
 * Mark all file descriptors (except stdin/stdout/stderr) as close-on-exec.
 * This is simpler than closing them directly because we don't need to track
 * which FDs to skip (status_fd, dir_fd, etc.) - they all get CLOEXEC and
 * will be closed automatically when we exec.
 *
 * Tries /proc/self/fd first (Linux), falls back to iterating to RLIMIT_NOFILE.
 */
static void mark_all_cloexec(void)
{
  DIR *dir;
  struct dirent *entry;
  int fd;

  dir = opendir("/proc/self/fd");
  if (dir != NULL)
    {
      while ((entry = readdir(dir)) != NULL)
        {
          if (entry->d_name[0] == '.')
            continue;

          fd = atoi(entry->d_name);

          /* Don't mark stdin/stdout/stderr - these must stay open after exec */
          if (fd == STDIN_FILENO || fd == STDOUT_FILENO || fd == STDERR_FILENO)
            continue;

          fcntl(fd, F_SETFD, FD_CLOEXEC);
        }
      closedir(dir);
    }
  else
    {
      /* Fallback: iterate up to RLIMIT_NOFILE */
      struct rlimit rl;
      int max_fd;

      if (getrlimit(RLIMIT_NOFILE, &rl) == 0 && rl.rlim_cur != RLIM_INFINITY)
        max_fd = (int)rl.rlim_cur;
      else
        max_fd = 1024;  /* Reasonable default */

      for (fd = 3; fd < max_fd; fd++)
        fcntl(fd, F_SETFD, FD_CLOEXEC);
    }
}

int main(int argc, char *argv[])
{
  int status_fd;
  int stdin_fd;
  int stdout_fd;
  int stderr_fd;
  char *cwd;
  char *cmd;
  char **args;

  /* Validate argument count */
  if (argc < HELPER_ARGC_MIN)
    {
      /* No status_fd available for proper error reporting */
      _exit(127);
    }

  /* Parse arguments */
  status_fd = atoi(argv[1]);
  stdin_fd  = atoi(argv[2]);
  stdout_fd = atoi(argv[3]);
  stderr_fd = atoi(argv[4]);
  cwd       = argv[5];
  cmd       = argv[6];
  args      = &argv[6];  /* cmd is argv[0] for target */

  /* Validate status_fd */
  if (fcntl(status_fd, F_GETFD) < 0)
    _exit(127);

  /*
   * Mark all FDs except 0/1/2 as close-on-exec. This includes status_fd
   * (so it closes on successful exec) and any other inherited FDs.
   * The stdin_fd/stdout_fd/stderr_fd will also be marked, but that's fine
   * because dup2() creates new FDs 0/1/2 without the CLOEXEC flag.
   */
  mark_all_cloexec();

  /* Redirect stdio. The original FDs have CLOEXEC set, so they'll be
   * closed automatically on exec. We don't close them here because
   * stdout_fd and stderr_fd may be the same FD (when stderr is redirected
   * to stdout), and closing after the first dup2 would break the second.
   */
  if (stdin_fd != STDIN_FILENO)
    {
      if (dup2(stdin_fd, STDIN_FILENO) < 0)
        report_error_and_exit(status_fd, errno);
    }

  if (stdout_fd != STDOUT_FILENO)
    {
      if (dup2(stdout_fd, STDOUT_FILENO) < 0)
        report_error_and_exit(status_fd, errno);
    }

  if (stderr_fd != STDERR_FILENO)
    {
      if (dup2(stderr_fd, STDERR_FILENO) < 0)
        report_error_and_exit(status_fd, errno);
    }

  /* Change working directory if specified */
  if (cwd[0] != '\0')
    {
      if (chdir(cwd) < 0)
        report_error_and_exit(status_fd, errno);
    }

  /* Execute target using PATH search. The helper inherits the correct
   * environment from the parent's execve(), so execvp will search PATH
   * using that environment. This fixes the original bug where PATH was
   * not searched when a custom environment was passed.
   */
  execvp(cmd, args);

  /* exec failed - report error */
  report_error_and_exit(status_fd, errno);

  /* Not reached */
  return 127;
}
