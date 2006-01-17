/* taret_generic_memory.h - Native methods for generic memory operations
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

#ifndef __TARGET_GENERIC_MEMORY__
#define __TARGET_GENERIC_MEMORY__

/* check if target_native_io.h included */
#ifndef __TARGET_NATIVE_MEMORY__
  #error Do NOT INCLUDE generic target files! Include the corresponding native target files instead!
#endif

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <assert.h>
#if HAVE_SYS_MMAN_H
  #include <sys/mman.h>
#endif

#include "target_native.h"

#ifdef NEW_CP
#include "../posix/target_posix_memory.h"
#endif

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/
#ifndef NEW_CP
#ifndef TARGET_NATIVE_MEMORY_MAP_PROTECTION_NONE
  #define TARGET_NATIVE_MEMORY_MAP_PROTECTION_NONE    PROT_NONE
#endif
#ifndef TARGET_NATIVE_MEMORY_MAP_PROTECTION_READ
  #define TARGET_NATIVE_MEMORY_MAP_PROTECTION_READ    PROT_READ
#endif
#ifndef TARGET_NATIVE_MEMORY_MAP_PROTECTION_WRITE
  #define TARGET_NATIVE_MEMORY_MAP_PROTECTION_WRITE   PROT_WRITE
#endif
#ifndef TARGET_NATIVE_MEMORY_MAP_PROTECTION_EXECUTE
  #define TARGET_NATIVE_MEMORY_MAP_PROTECTION_EXECUTE PROC_EXEC
#endif

#ifndef TARGET_NATIVE_MEMORY_MAP_FLAGS_FIXED
  #define TARGET_NATIVE_MEMORY_MAP_FLAGS_FIXED   MAP_FIXED
#endif
#ifndef TARGET_NATIVE_MEMORY_MAP_FLAGS_SHARED
  #define TARGET_NATIVE_MEMORY_MAP_FLAGS_SHARED  MAP_SHARED
#endif
#ifndef TARGET_NATIVE_MEMORY_MAP_FLAGS_PRIVATE
  #define TARGET_NATIVE_MEMORY_MAP_FLAGS_PRIVATE MAP_PRIVATE
#endif
#else
#ifndef TARGET_NATIVE_MEMORY_MAP_PROTECTION_NONE
  #define TARGET_NATIVE_MEMORY_MAP_PROTECTION_NONE    CP_MEMORY_MAP_PROTECTION_NONE
#endif
#ifndef TARGET_NATIVE_MEMORY_MAP_PROTECTION_READ
  #define TARGET_NATIVE_MEMORY_MAP_PROTECTION_READ    CP_MEMORY_MAP_PROTECTION_READ
#endif
#ifndef TARGET_NATIVE_MEMORY_MAP_PROTECTION_WRITE
  #define TARGET_NATIVE_MEMORY_MAP_PROTECTION_WRITE   CP_MEMORY_MAP_PROTECTION_WRITE
#endif
#ifndef TARGET_NATIVE_MEMORY_MAP_PROTECTION_EXECUTE
  #define TARGET_NATIVE_MEMORY_MAP_PROTECTION_EXECUTE CP_MEMORY_MAP_PROTECTION_EXECUTE
#endif

#ifndef TARGET_NATIVE_MEMORY_MAP_FLAGS_FIXED
  #define TARGET_NATIVE_MEMORY_MAP_FLAGS_FIXED   CP_MEMORY_MAP_FLAGS_FIXED
#endif
#ifndef TARGET_NATIVE_MEMORY_MAP_FLAGS_SHARED
  #define TARGET_NATIVE_MEMORY_MAP_FLAGS_SHARED  CP_MEMORY_MAP_FLAGS_SHARED
#endif
#ifndef TARGET_NATIVE_MEMORY_MAP_FLAGS_PRIVATE
  #define TARGET_NATIVE_MEMORY_MAP_FLAGS_PRIVATE CP_MEMORY_MAP_FLAGS_PRIVATE
#endif
#endif

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : TARGET_NATIVE_MEMORY_ALLOC
* Purpose    : allocate memory
* Input      : type - type definition (use for cast)
*              size - size of memory (in bytes)
* Output     : p - allocated memory or NULL if insufficient memory
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_MEMORY_ALLOC
  #include <string.h>
  #define TARGET_NATIVE_MEMORY_ALLOC(p,type,size) \
    do { \
      p=(type)malloc(size); \
    } while(0)
#endif
#else /* NEW_CP */
    #define TARGET_NATIVE_MEMORY_ALLOC(p,type,size) \
      CP_MEMORY_ALLOC(p,type,size)
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_MEMORY_REALLOC
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

#ifndef NEW_CP
#ifndef TARGET_NATIVE_MEMORY_REALLOC
  #include <string.h>
  #define TARGET_NATIVE_MEMORY_REALLOC(oldp,newp,type,oldSize,newSize) \
    do { \
      newp=(type)realloc(oldp,newSize); \
    } while(0)
#endif
#else /* NEW_CP */
    #define TARGET_NATIVE_MEMORY_REALLOC(oldp,newp,type,oldSize,newSize) \
      CP_MEMORY_REALLOC(oldp,newp,type,oldSize,newSize)
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_MEMORY_FREE
* Purpose    : free memory
* Input      : p - pointer to memory to free
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_MEMORY_FREE
  #include <string.h>
  #include <assert.h>
  #define TARGET_NATIVE_MEMORY_FREE(p) \
    do { \
      assert(p!=NULL); \
      free(p); \
    } while(0)
#endif
#else /* NEW_CP */
    #define TARGET_NATIVE_MEMORY_FREE(p) \
      CP_MEMORY_FREE(p)
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_MEMORY_FILL
* Purpose    : fill memory
* Input      : p    - pointer to memory to fill
*              value - value for fill
*              size  - size to fill
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_MEMORY_FILL
  #include <string.h>
  #define TARGET_NATIVE_MEMORY_FILL(p,value,size) \
    do { \
      memset(p,value,size); \
    } while(0)
#endif
#else /* NEW_CP */
    #define TARGET_NATIVE_MEMORY_FILL(p,value,size) \
      CP_MEMORY_FILL(p,value,size)
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_MEMORY_FILL_INT32
* Purpose    : fill memory with int32-values
* Input      : p    - pointer to memory to fill
*              value - int32-value for fill
*              n     - number of int32 to fill
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_MEMORY_FILL_INT32
  #include <string.h>
  #define TARGET_NATIVE_MEMORY_FILL_INT32(p,value,n) \
    do { \
      register u_int32_t *__p; \
      register int       __n; \
      \
      __p=(u_int32_t*)p; \
      for (__n = 0; __n < n; __n++) \
      { \
        (*__p) = value; \
        __p++; \
      } \
    } while(0)
#endif
#else /* NEW_CP */
    #define TARGET_NATIVE_MEMORY_FILL_INT32(p,value,n) \
      CP_MEMORY_FILL_INT32(p,value,n)
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_MEMORY_COPY
* Purpose    : copy memory (memory may overlap)
* Input      : source      - pointer to memory source
*              destination - pointer to memory destination
*              size        - size to copy (bytes)
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_MEMORY_COPY
  #include <string.h>
  #define TARGET_NATIVE_MEMORY_COPY(source,destination,size) \
    do { \
      memmove(destination,source,size); \
    } while(0)
#endif
#else /* NEW_CP */
    #define TARGET_NATIVE_MEMORY_COPY(source,destination,size) \
      CP_MEMORY_COPY(source,destination,size)
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_MEMORY_FAST_COPY
* Purpose    : fast copy memory (non-overlapping memory!)
* Input      : source      - pointer to memory source
*              destination - pointer to memory destination
*              size        - size to copy (bytes)
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

/* NYI: OPTIMIZATION: assembly-inline? */

#ifndef NEW_CP
#ifndef TARGET_NATIVE_MEMORY_FAST_COPY
  #include <string.h>
  #define TARGET_NATIVE_MEMORY_FAST_COPY(source,destination,size) \
    do { \
      memcpy(destination,source,size); \
    } while(0)
#endif
#else /* NEW_CP */
    #define TARGET_NATIVE_MEMORY_FAST_COPY(source,destination,size) \
      CP_MEMORY_FAST_COPY(source,destination,size)
#endif /* NEW_CP */

/*---------------------------------------------------------------------*/

/***********************************************************************\
* Name       : TARGET_NATIVE_MEMORY_MAP
* Purpose    : map file into memory
* Input      : filedescriptor - filedescriptor
*              start          - memory address for mapping
*              length         - length to map
*              offset         - file offset
*              protection     - protection flags
*              flags          - flags
* Output     : result - memory address or NULL on error
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_MEMORY_MAP
  #include <string.h>
  #ifdef HAVE_SYS_MMAN_H
    #include <sys/mman.h>
  #endif
  #ifdef HAVE_MMAP
    #define TARGET_NATIVE_MEMORY_MAP(filedescriptor,start,length,offset,protection,flags,result) \
      do { \
        result = mmap(start,length,protection,flags,filedescriptor,offset); \
      } while(0)
  #else
    #define TARGET_NATIVE_MEMORY_MAP(filedescriptor,start,length,offset,protection,flags,result) \
      do { \
        result = NULL; \
      } while(0)
  #endif
#endif
#else /* NEW_CP */
    #define TARGET_NATIVE_MEMORY_MAP(filedescriptor,start,length,offset,protection,flags,result) \
      CP_MEMORY_MAP(filedescriptor,start,length,offset,protection,flags,result)
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_MEMORY_UNMAP
* Purpose    : unmap file from memory
* Input      : start  - memory address for mapping
*              length - length to map
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_MEMORY_UNMAP
  #include <string.h>
  #ifdef HAVE_SYS_MMAN_H
    #include <sys/mman.h>
  #endif
  #ifdef HAVE_MUNMAP
    #define TARGET_NATIVE_MEMORY_UNMAP(start,length,result) \
      do { \
         result = (munmap(start,length)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while(0)
  #else
    #define TARGET_NATIVE_MEMORY_UNMAP(start,length,result) \
      do { \
        result = TARGET_NATIVE_ERROR; \
      } while(0)
  #endif
#endif
#else /* NEW_CP */
    #define TARGET_NATIVE_MEMORY_UNMAP(start,length,result) \
      CP_MEMORY_UNMAP(start,length,result)
#endif /* NEW_CP */

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_GENERIC_MEMORY__ */

/* end of file */
