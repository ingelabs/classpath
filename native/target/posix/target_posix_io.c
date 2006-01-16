/* generic_posix_io.c - Native methods for POSIX I/O operations
   Copyright (C) 2006 Free Software Foundation, Inc.

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
Description: generic target defintions of miscellaneous functions
Systems    : all
*/

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <stdio.h>
#include <stdarg.h>
#include <assert.h>

#include "jni.h"

#include "target_posix.h"

#include "target_posix_io.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef NEW_CP

#ifdef CP_IO_READ_STDIN_POSIX
  #include <unistd.h>
  int cp_io_read_stdin(void *buffer, unsigned int length, unsigned int *bytesRead)
    {
      assert(bytesRead != NULL);

      (*bytesRead) = read(0,buffer,length);

      return ((*bytesRead) != -1)?CP_OK:CP_ERROR;
    }
#endif /* CP_IO_READ_STDIN_POSIX */

#ifdef CP_IO_WRITE_STDOUT_POSIX
  #include <unistd.h>
  int cp_io_write_stdout(const void *buffer, unsigned int length, unsigned int *bytesWritten)
    {
      assert(bytesWritten != NULL);

      (*bytesWritten) = write(1,buffer,length);

      return ((*bytesWritten) != -1)?CP_OK:CP_ERROR;
    }
#endif /* CP_IO_WRITE_STDOUT_POSIX */

#ifdef CP_IO_WRITE_STDERR_POSIX
  #include <unistd.h>
  int cp_io_write_stderr(const void *buffer, unsigned int length, unsigned int *bytesWritten)
    {
      assert(bytesWritten != NULL);

      (*bytesWritten) = write(2,buffer,length);

      return ((*bytesWritten) != -1)?CP_OK:CP_ERROR;
    }
#endif /* CP_IO_WRITE_STDERR_POSIX */

#if 0
/* still not active, because of strange macro problem x=(CP_IO_PRINT_ERROR(...),b) */
  #ifdef x__GNUC__
    #include <stdio.h>
    #define CP_IO_PRINT(format,Args...) \
      fprintf(stdout,format, ## Args)
  #else
    #define CP_IO_PRINT_GENERIC
    #define CP_IO_PRINT targetGenericIO_printf
  #endif
#endif /* 0 */

#ifdef CP_IO_PRINT_POSIX
  #include <stdio.h>
  int cp_io_printf(const char *Format, ...)
    {
      va_list Arguments;
      int     n;

      assert(Format != NULL);

      va_start(Arguments,Format);
      n = vprintf(Format,Arguments);
      va_end(Arguments);

      return n;
    }
#endif /* CP_IO_PRINT_POSIX */

#ifdef CP_IO_PRINT_ERROR_POSIX
  #include <stdio.h>
  int cp_io_printf_stderr(const char *Format, ...)
    {
      va_list Arguments;
      int     n;

      assert(Format != NULL);

      va_start(Arguments,Format);
      n = vfprintf(stderr,Format,Arguments);
      va_end(Arguments);

      return(n);
    }
#endif /* CP_IO_PRINT_ERROR_GENERIC */

#endif /* NEW_CP */

#ifdef __cplusplus
}
#endif

/* end of file */
