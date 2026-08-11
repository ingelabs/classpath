/* cpspawnhelper.c -
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

#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "cpproc-child.h"

/* Bound the last-resort fcntl scan when OPEN_MAX is pathologically large. */
#define MAX_FD_SCAN 65536

static int get_max_fd(void)
{
  long value = sysconf(_SC_OPEN_MAX);

  if (value <= 0 || value > MAX_FD_SCAN)
    return MAX_FD_SCAN;

  return (int) value;
}

static void report_errnum(int fail_fd, int errnum)
{
  while (write(fail_fd, &errnum, sizeof(errnum)) < 0
	 && errno == EINTR)
    ;
}

static int read_full(int fd, void *buf, size_t len)
{
  char *p = buf;
  size_t off = 0;

  while (off < len)
    {
      ssize_t n = read(fd, p + off, len - off);

      if (n < 0)
	{
	  if (errno == EINTR)
	    continue;
	  return errno;
	}
      if (n == 0)
	return EPIPE;		/* the data was never sent in full */
      off += (size_t) n;
    }

  return 0;
}

/* Read the target environment from the env pipe. On success,
   stores the environment and returns 0. On error, returns an errno.
   See cpproc-child.h for the layout. */
static int read_spawn_env(int fd, char ***out_env)
{
  int header[2];
  size_t len;
  size_t off;
  size_t count;
  char *blob;
  char **env;
  int err;
  int i;

  err = read_full(fd, header, sizeof(header));
  if (err != 0)
    return err;

  if (header[0] != CPPROC_SPAWN_MAGIC || header[1] < 0)
    return EINVAL;

  len = (size_t) header[1];
  blob = malloc(len + 1);
  if (blob == NULL)
    return ENOMEM;

  err = read_full(fd, blob, len);
  if (err != 0)
    {
      free(blob);
      return err;
    }

  /* The strings are walked with strlen(), so require the block to end
     in NUL to prevent the final scan from reading past it. */
  if (len > 0 && blob[len - 1] != '\0')
    {
      free(blob);
      return EINVAL;
    }
  blob[len] = '\0';

  count = 0;
  for (off = 0; off < len; off += strlen(blob + off) + 1)
    count++;

  env = malloc((count + 1) * sizeof(char *));
  if (env == NULL)
    {
      free(blob);
      return ENOMEM;
    }

  off = 0;
  for (i = 0; i < (int) count; i++)
    {
      env[i] = blob + off;
      off += strlen(blob + off) + 1;
    }
  env[count] = NULL;

  *out_env = env;
  return 0;
}

int main(int argc, char **argv)
{
  char * const *target_argv;
  char **target_envp;
  char **sh_argv;
  const char *path;
  const char *wd;
  int target_argc;
  int stdin_fd;
  int stdout_fd;
  int stderr_fd;
  int fail_fd;
  int env_fd;
  int err;

  if (argc < 9)
    {
      fprintf(stderr,
	      "Usage: %s stdin-fd stdout-fd stderr-fd fail-fd env-fd wd path program [args...]\n",
	      argv[0]);
      /* No fail fd to report through; just exit. */
      return CPPROC_EXIT_ERROR;
    }

  stdin_fd = atoi(argv[1]);
  stdout_fd = atoi(argv[2]);
  stderr_fd = atoi(argv[3]);
  fail_fd = atoi(argv[4]);
  env_fd = atoi(argv[5]);
  /* wd == "." means no directory change. */
  wd = (strcmp(argv[6], ".") == 0) ? NULL : argv[6];
  path = argv[7];
  target_argv = &argv[8];

  /* Tell the parent we are alive, before doing anything that may
     block or fail. */
  report_errnum(fail_fd, CPPROC_HELPER_ALIVE);

  err = read_spawn_env(env_fd, &target_envp);
  if (err != 0)
    {
      report_errnum(fail_fd, err);
      return CPPROC_EXIT_ERROR;
    }
  close(env_fd);

  for (target_argc = 0; target_argv[target_argc] != NULL; target_argc++)
    ;
  sh_argv = malloc((target_argc + 2) * sizeof(char *));
  if (sh_argv == NULL)
    {
      report_errnum(fail_fd, ENOMEM);
      return CPPROC_EXIT_ERROR;
    }

  cpproc_child_exec(target_argv, target_envp, stdin_fd, stdout_fd,
		    stderr_fd, fail_fd, path, sh_argv, wd, get_max_fd());

  /* cpproc_child_exec() does not return. */
  return CPPROC_EXIT_ERROR;
}
