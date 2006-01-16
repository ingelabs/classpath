/* target_posix_io.h - Native methods for POSIX I/O operations
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

#ifndef __TARGET_POSIX_IO__
#define __TARGET_POSIX_IO__

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <assert.h>

#include "jni.h"

#include "target_native.h"

#include "target_posix.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : CP_IO_READ_STDIN
* Purpose    : read data from stdin
* Input      : buffer - buffer
*              length - number of bytes to read
* Output     : bytesRead - bytes read
*              result    - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_IO_READ_STDIN
  #define CP_IO_READ_STDIN_POSIX
  #define CP_IO_READ_STDIN(buffer,length,bytesRead,result) \
    (result) = cp_io_read_stdin(buffer,length,&bytesRead)
#endif

/***********************************************************************\
* Name       : CP_IO_WRITE_STDOUT
* Purpose    : write data to stdout
* Input      : buffer - buffer
*              length - number of bytes to read
* Output     : bytesWritten - bytes written
*              result       - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_IO_WRITE_STDOUT
  #define CP_IO_WRITE_STDOUT_POSIX
  #define CP_IO_WRITE_STDOUT(buffer,length,bytesWritten,result) \
    (result) = cp_io_write_stdout(buffer,length,&bytesWritten)
#endif

/***********************************************************************\
* Name       : write data to stderr
* Purpose    : write data to stderr
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_IO_WRITE_STDERR
  #define CP_IO_WRITE_STDERR_POSIX
  #define CP_IO_WRITE_STDERR(buffer,length,bytesWritten,result) \
    (result) = cp_io_write_stderr(buffer,length,&bytesWritten)
#endif

/*---------------------------------------------------------------------*/

/***********************************************************************\
* Name       : CP_IO_PRINT
* Purpose    : print formated string to stdout
* Input      : -
* Output     : -
* Return     : number of characters printed
* Side-effect: unknown
* Notes      : Even if printf() is an ANSI-C function a macro is used
*              to be able to use some other output channels than stdout
*              (e. g. a file or a serial port).
*
*              Hint: for non-gcc compilers only then name is defined and
*              a function is inserted, because of variable number of
*              arguments. Only the preprocessor of gcc support a
*              feature for defining macros with variable parameters. To
*              be able to use an other compiler the macro only replaces
*              the name with some function implementing the output of a
*              string with variable parameters (like printf)
\***********************************************************************/

#ifndef CP_IO_PRINT
  #define CP_IO_PRINT_POSIX
// NYI: UNDER DEVELOPMENT: broken if macro is called inside another macro?
  #ifdef xxxCPP_VARARGS
    #define CP_IO_PRINT(format,Args...) \
      cp_io_printf(format, ## Args)
  #else
    #define CP_IO_PRINT \
      cp_io_printf
  #endif
#endif

/***********************************************************************\
* Name       : CP_IO_PRINT_ERROR
* Purpose    : print formated string to stderr
* Input      : -
* Output     : -
* Return     : number of characters printed
* Side-effect: unknown
* Notes      : Even if printf() is an ANSI-C function a macro is used
*              to be able to use some other output channels than stderr
*              (e. g. a file or a serial port).
*
*              Hint: for non-gcc compilers only then name is defined and
*              a function is inserted, because of variable number of
*              arguments. Only the preprocessor of gcc support a
*              feature for defining macros with variable parameters. To
*              be able to use an other compiler the macro only replaces
*              the name with some function implementing the output of a
*              string with variable parameters (like printf)
\***********************************************************************/

#ifndef CP_IO_PRINT_ERROR
  #define CP_IO_PRINT_ERROR_POSIX
// NYI: UNDER DEVELOPMENT: broken if macro is called inside another macro?
  #ifdef xxxCPP_VARARGS
    #define CP_IO_PRINT_ERROR(format,Args...) \
      cp_io_printf_stderr(format, ## Args)
  #else
    #define CP_IO_PRINT_ERROR \
      cp_io_printf_stderr
  #endif
#endif

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

int cp_io_read_stdin(void *buffer, unsigned int length, unsigned int *bytesRead);
int cp_io_write_stdout(const void *buffer, unsigned int length, unsigned int *bytesWritten);
int cp_io_write_stderr(const void *buffer, unsigned int length, unsigned int *bytesWritten);
int cp_io_printf(const char *Format, ...);
int cp_io_printf_stderr(const char *Format, ...);

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_POSIX_IO__ */

/* end of file */
