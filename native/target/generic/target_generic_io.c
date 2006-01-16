/* target_generic_io.c - Native methods generic I/O operations
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
Description: generic target definotions of I/O functions
Systems    : all
*/

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <stdio.h>
#include <stdarg.h>
#include <assert.h>

#include "target_native.h"

#include "target_native_io.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef TARGET_NATIVE_IO_PRINT_GENERIC
int targetGenericIO_printf(const char *format, ...)
{
  va_list arguments;
  int     n;

  assert(format!=NULL);

  va_start(arguments,format);
  n=vprintf(format,arguments);
  va_end(arguments);

  /* TEMPORARY: make sure new Unix_printf works like old jprint */
  fflush(stdout);

  return(n);
}
#endif /* TARGET_NATIVE_IO_PRINT_GENERIC */

#ifdef TARGET_NATIVE_IO_PRINT_ERROR_GENERIC
int targetGenericIO_printf_stderr(const char *format, ...)
{
  va_list arguments;
  int     n;

  assert(format!=NULL);

  va_start(arguments,format);
  n=vfprintf(stderr,format,arguments);
  va_end(arguments);

  /* we need to flush stdout since otherwise stderr is not displayed */
  fflush(stderr);

  return(n);
}
#endif /* TARGET_NATIVE_IO_PRINT_ERROR_GENERIC */

#ifdef __cplusplus
}
#endif

/* end of file */
