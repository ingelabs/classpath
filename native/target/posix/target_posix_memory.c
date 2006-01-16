/* target_posix_memory.c - Native methods for POSIX memory operations
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

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#ifdef HAVE_SYS_TYPES_H
#include <sys/types.h>
#endif
#include <assert.h>

#include "target_posix.h"

#include "target_posix_memory.h"

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

#ifdef CP_MEMORY_ALLOC_POSIX
  void *cp_memory_alloc(unsigned long size)
    {
      return malloc(size);
    }
#endif

#ifdef CP_MEMORY_REALLOC_POSIX
  void *cp_memory_realloc(void *p, unsigned long size)
    {
      return realloc(p,size);
    }
#endif

#ifdef CP_MEMORY_FREE_POSIX
  void cp_memory_free(void *p)
    {
      assert(p != NULL);

      free(p);
    }
#endif

#ifdef CP_MEMORY_FILL_POSIX
  #include <string.h>
  void cp_memory_fill(void *p, char value, unsigned long size)
    {
      memset(p,value,size);
    }
#endif

#ifdef CP_MEMORY_FILL_INT32_POSIX
  #include <string.h>
  void cp_memory_fill_int32(void *p, u_int32_t value, unsigned long n)
    {
      register u_int32_t *t;
      register int       i;

      t=(u_int32_t*)p;
      for (i = 0; i < n; i++)
      {
        (*t) = value;
        t++;
      }
    }
#endif

#ifdef CP_MEMORY_COPY_POSIX
  #include <string.h>
  void cp_memory_copy(const void *source, void *destination, unsigned long size)
    {
      memmove(destination,source,size);
    }
#endif

#ifdef CP_MEMORY_FAST_COPY_POSIX
  #include <string.h>
  void cp_memory_fastCopy(const void *source, void *destination, unsigned long size)
    {
      memcpy(destination,source,size);
    }
#endif

#ifdef CP_MEMORY_MAP_POSIX
  #ifdef HAVE_SYS_MMAN_H
    #include <sys/mman.h>
  #endif
  void *cp_memory_map(int filedescriptor, void *start, unsigned long length, unsigned long offset, int protection, int flags)
    {
      return mmap(start,length,protection,flags,filedescriptor,offset);
    }
#endif

#ifdef CP_MEMORY_UNMAP_POSIX
  #ifdef HAVE_SYS_MMAN_H
    #include <sys/mman.h>
  #endif
  int cp_memory_unmap(void *start, unsigned long length)
    {
      return (munmap(start,length)==0)?CP_OK:CP_ERROR;
    }
#endif

#endif /* NEW_CP */

#ifdef __cplusplus
}
#endif

/* end of file */
