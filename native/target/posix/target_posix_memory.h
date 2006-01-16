/* target_posix_memory.h - Native methods for POSIX memory operations
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
Description: generic target defintions of memory functions
Systems    : all
*/

#ifndef __TARGET_POSIX_MEMORY__
#define __TARGET_POSIX_MEMORY__

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#ifdef HAVE_SYS_TYPES_H
#include <sys/types.h>
#endif
#ifdef HAVE_SYS_MMAP_H
#include <sys/mmap.h>
#endif
#include <assert.h>

#include "target_posix.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/
#define CP_MEMORY_MAP_PROTECTION_NONE    PROT_NONE
#define CP_MEMORY_MAP_PROTECTION_READ    PROT_READ
#define CP_MEMORY_MAP_PROTECTION_WRITE   PROT_WRITE
#define CP_MEMORY_MAP_PROTECTION_EXECUTE PROC_EXEC

#define CP_MEMORY_MAP_FLAGS_FIXED   MAP_FIXED
#define CP_MEMORY_MAP_FLAGS_SHARED  MAP_SHARED
#define CP_MEMORY_MAP_FLAGS_PRIVATE MAP_PRIVATE

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : CP_MEMORY_ALLOC
* Purpose    : allocate memory
* Input      : type - type definition (use for cast)
*              size - size of memory (in bytes)
* Output     : p - allocated memory or NULL if insufficient memory
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_MEMORY_ALLOC
  #define CP_MEMORY_ALLOC_POSIX
  #define CP_MEMORY_ALLOC(p,type,size) \
    (p) = (type)cp_memory_alloc(size)
#endif

/***********************************************************************\
* Name       : CP_MEMORY_REALLOC
* Purpose    : re-allocate memory (decrease/increase size)
* Input      : oldp    - allocated memory
*              type    - type definition (use for cast)
*              oldSize - old size of memory (in bytes)
*              newSize - new size of memory (in bytes)
* Output     : newp - new allocated memory or NULL if insufficient memory
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_MEMORY_REALLOC
  #define CP_MEMORY_REALLOC_POSIX
  #define CP_MEMORY_REALLOC(oldp,newp,type,oldSize,newSize) \
    (newp) = (type)cp_memory_realloc(oldp,newSize)
#endif

/***********************************************************************\
* Name       : CP_MEMORY_FREE
* Purpose    : free memory
* Input      : p - pointer to memory to free
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_MEMORY_FREE
  #define CP_MEMORY_FREE_POSIX
  #define CP_MEMORY_FREE(p) \
    cp_memory_free(p)
#endif

/***********************************************************************\
* Name       : CP_MEMORY_FILL
* Purpose    : fill memory
* Input      : p     - pointer to memory to fill
*              value - value for fill
*              size  - size to fill
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_MEMORY_FILL
  #define CP_MEMORY_FILL_POSIX
  #define CP_MEMORY_FILL(p,value,size) \
    cp_memory_fill(p,value,size)
#endif

/***********************************************************************\
* Name       : CP_MEMORY_FILL_INT32
* Purpose    : fill memory with int32 values
* Input      : p     - pointer to memory to fill
*              value - int32-value for fill
*              n     - number of values to fill
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_MEMORY_FILL_INT32
  #define CP_MEMORY_FILL_INT32_POSIX
  #define CP_MEMORY_FILL_INT32(p,value,size) \
    cp_memory_fill_int32(p,value,n)
#endif

/***********************************************************************\
* Name       : CP_MEMORY_COPY
* Purpose    : copy memory (memory blocks may overlap)
* Input      : source      - pointer to memory source
*              destination - pointer to memory destination
*              size        - size to copy (bytes)
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_MEMORY_COPY
  #define CP_MEMORY_COPY_POSIX
  #define CP_MEMORY_COPY(source,destination,size) \
    cp_memory_copy(source,destination,size)
#endif

/***********************************************************************\
* Name       : CP_MEMORY_FAST_COPY
* Purpose    : copy memory (non-overlapping memory blocks!)
* Input      : source      - pointer to memory source
*              destination - pointer to memory destination
*              size        - size to copy (bytes)
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_MEMORY_FAST_COPY
  #define CP_MEMORY_FAST_COPY_POSIX
  #define CP_MEMORY_FAST_COPY(source,destination,size) \
    cp_memory_fastCopy(source,destination,size)
#endif

/*---------------------------------------------------------------------*/

/***********************************************************************\
* Name       : CP_MEMORY_MAP
* Purpose    : map file into memory
* Input      : filedescriptor - filedescriptor
*              start          - memory address for mapping
*              length         - length to map
*              offset         - file offset
*              protection     - protection flags
*              flags          - flags
* Output     : result - memory address or NULL
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_MEMORY_MAP
  #define CP_MEMORY_MAP_POSIX
  #define CP_MEMORY_MAP(filedescriptor,start,length,offset,protection,flags,result) \
    result = cp_memory_map(filedescriptor,start,length,offset,protection,flags)
#endif

/***********************************************************************\
* Name       : CP_MEMORY_UNMAP
* Purpose    : unmap file from memory
* Input      : start  - memory address for mapping
*              length - length to map
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_MEMORY_UNMAP
  #define CP_MEMORY_UNMAP_POSIX
  #define CP_MEMORY_UNMAP(start,length,result) \
    result = cp_memory_unmap(start,length)
#endif

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

void *cp_memory_alloc(unsigned long size);
void *cp_memory_realloc(void *p, unsigned long size);
void cp_memory_free(void *p);
void cp_memory_fill(void *p, char value, unsigned long size);
void cp_memory_fill_int32(void *p, u_int32_t value, unsigned long n);
void cp_memory_copy(const void *source, void *destination, unsigned long size);
void cp_memory_fastCopy(const void *source, void *destination, unsigned long size);

void *cp_memory_map(int filedescriptor, void *start, unsigned long length, unsigned long offset, int protection, int flags);
int cp_memory_unmap(void *start, unsigned long length);

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_POSIX_MEMORY__ */

/* end of file */
