/* target_native_io.c - Native methods for I/O operations
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
Description: embOS target defintions of I/O-functions
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

int targetNativeembOS_print(const char *buffer, int length)
{
  static char tmpBuffer[512];
  int         z1,z2;

  assert(buffer!=NULL);

  /* convert \n -> \r\n */
  z1=0;
  z2=0;
  while ((z1<length) && (z2<sizeof(tmpBuffer)-2))
  {
    if (buffer[z1]=='\n')
    {
      tmpBuffer[z2]='\r';z2++;
    }
    tmpBuffer[z2]=buffer[z1];;
    z1++;
    z2++;
  }
  tmpBuffer[z2]='\0';

  /* output */
  tx_putstr(2,tmpBuffer);

  return z2;
}

int targetNativeembOS_printf(const char *format, ...)
{
  va_list arguments;
  int     n;

  assert(format!=NULL);

  /* format string */
  va_start(arguments,format);
  n = vsnprintf(buffer,sizeof(buffer),format,arguments);
  va_end(arguments);

  /* output */
  n = JamaicaNativeembOS_print(buffer,n);

  return(n);
}

#ifdef __cplusplus
}
#endif

/* end of file */
