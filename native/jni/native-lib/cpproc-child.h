/* cpproc-child.h -
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

#ifndef _CPPROC_CHILD_H
#define _CPPROC_CHILD_H

#include <sys/types.h>

/* These functions are internal to the native library, so keep them
   out of its dynamic symbol table. This is hygiene only: child-safety
   does not depend on visibility, so the fallback to default visibility
   is harmless. */
#if defined(__GNUC__)
#define CP_HIDDEN __attribute__((visibility("hidden")))
#else
#define CP_HIDDEN
#endif

/* Fork and exec the target program; returns the child pid, or -1
   with errno set if fork() fails. */
CP_HIDDEN pid_t cpproc_child_fork_exec(char * const *commandLine, char * const *newEnviron,
				       int *local_fds, int pipe_count, int *fail_fds,
				       const char *path, char **sh_argv, const char *wd,
				       int maxfd);

#endif
