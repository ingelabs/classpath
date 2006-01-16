/* target_generic_math.h - Native methods for math operations
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
Description: generic target defintions of int/int64 constants/
             macros/functions
Systems    : all
*/

#ifndef __TARGET_GENERIC_MATH__
#define __TARGET_GENERIC_MATH__

/* check if target_native_math.h included */
#ifndef __TARGET_NATIVE_MATH__
  #error Do NOT INCLUDE generic target files! Include the corresponding native target files instead!
#endif

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <assert.h>

#include "jni.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/
#ifndef TARGET_NATIVE_MATH_INT_INT64_CONST_0
  #define TARGET_NATIVE_MATH_INT_INT64_CONST_0 0LL
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_CONST_1
  #define TARGET_NATIVE_MATH_INT_INT64_CONST_1 1LL
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1
  #define TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1 -1LL
#endif

#ifndef TARGET_NATIVE_MATH_INT_UINT64_CONST_0
  #define TARGET_NATIVE_MATH_INT_UINT64_CONST_0 0LL
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_CONST_1
  #define TARGET_NATIVE_MATH_INT_UINT64_CONST_1 1LL
#endif

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/* math operations */
#ifndef TARGET_NATIVE_MATH_INT_INT64_ADD
  #define TARGET_NATIVE_MATH_INT_INT64_ADD(v1,v2) ((v1)+(v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_SUB
  #define TARGET_NATIVE_MATH_INT_INT64_SUB(v1,v2) ((v1)-(v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_MUL
  #define TARGET_NATIVE_MATH_INT_INT64_MUL(v1,v2) ((v1)*(v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_DIV
  #define TARGET_NATIVE_MATH_INT_INT64_DIV(v1,v2) ((v1)/(v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_MOD
  #define TARGET_NATIVE_MATH_INT_INT64_MOD(v1,v2) ((v1)%(v2))
#endif

#ifndef TARGET_NATIVE_MATH_INT_UINT64_ADD
  #define TARGET_NATIVE_MATH_INT_UINT64_ADD(v1,v2) ((v1)+(v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_SUB
  #define TARGET_NATIVE_MATH_INT_UINT64_SUB(v1,v2) ((v1)-(v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_MUL
  #define TARGET_NATIVE_MATH_INT_UINT64_MUL(v1,v2) ((v1)*(v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_DIV
  #define TARGET_NATIVE_MATH_INT_UINT64_DIV(v1,v2) ((v1)/(v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_MOD
  #define TARGET_NATIVE_MATH_INT_UINT64_MOD(v1,v2) ((v1)%(v2))
#endif

/* bit operations */
#ifndef TARGET_NATIVE_MATH_INT_INT64_AND
  #define TARGET_NATIVE_MATH_INT_INT64_AND(v1,v2) ((v1)&(v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_OR
  #define TARGET_NATIVE_MATH_INT_INT64_OR(v1,v2)  ((v1)|(v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_XOR
  #define TARGET_NATIVE_MATH_INT_INT64_XOR(v1,v2) ((v1)^(v2))
#endif

#ifndef TARGET_NATIVE_MATH_INT_UINT64_AND
  #define TARGET_NATIVE_MATH_INT_UINT64_AND(v1,v2) ((v1)&(v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_OR
  #define TARGET_NATIVE_MATH_INT_UINT64_OR(v1,v2)  ((v1)|(v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_XOR
  #define TARGET_NATIVE_MATH_INT_UINT64_XOR(v1,v2) ((v1)^(v2))
#endif

/* shift operations */
#ifndef TARGET_NATIVE_MATH_INT_INT64_SHIFTL
  #define TARGET_NATIVE_MATH_INT_INT64_SHIFTL(v,l)  ((v)<<(l))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_SHIFTR
  #define TARGET_NATIVE_MATH_INT_INT64_SHIFTR(v,l)  (((v)>>(l)) |  (((v)>=0) ? 0 : (0xffffFFFFffffFFFFLL << (64-(l)))))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_SHIFTR
  #define TARGET_NATIVE_MATH_INT_UINT64_SHIFTR(v,l) (((v)>>(l)) & ~(((v)>=0) ? 0 : (0xffffFFFFffffFFFFLL << (64-(l)))))
#endif

/* negation */
#ifndef TARGET_NATIVE_MATH_INT_INT64_NEG
  #define TARGET_NATIVE_MATH_INT_INT64_NEG(v) (-(v))
#endif

/* increment/decrement routines */
#ifndef TARGET_NATIVE_MATH_INT_INT64_INC
  #define TARGET_NATIVE_MATH_INT_INT64_INC(v) { v++; } 
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_DEC
  #define TARGET_NATIVE_MATH_INT_INT64_DEC(v) { v--; }
#endif

#ifndef TARGET_NATIVE_MATH_INT_UINT64_INC
  #define TARGET_NATIVE_MATH_INT_UINT64_INC(v) { v++; }
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_DEC
  #define TARGET_NATIVE_MATH_INT_UINT64_DEC(v) { v--; } 
#endif

/* comparison routines */
#ifndef TARGET_NATIVE_MATH_INT_INT64_EQ
  #define TARGET_NATIVE_MATH_INT_INT64_EQ(v1,v2) ((v1) == (v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_NE
  #define TARGET_NATIVE_MATH_INT_INT64_NE(v1,v2) ((v1) != (v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_LT
  #define TARGET_NATIVE_MATH_INT_INT64_LT(v1,v2) ((v1) <  (v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_LE
  #define TARGET_NATIVE_MATH_INT_INT64_LE(v1,v2) ((v1) <= (v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_GT
  #define TARGET_NATIVE_MATH_INT_INT64_GT(v1,v2) ((v1) >  (v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_GE
  #define TARGET_NATIVE_MATH_INT_INT64_GE(v1,v2) ((v1) >= (v2))
#endif

#ifndef TARGET_NATIVE_MATH_INT_UINT64_EQ
  #define TARGET_NATIVE_MATH_INT_UINT64_EQ(v1,v2) ((v1) == (v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_NE
  #define TARGET_NATIVE_MATH_INT_UINT64_NE(v1,v2) ((v1) != (v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_LT
  #define TARGET_NATIVE_MATH_INT_UINT64_LT(v1,v2) ((v1) <  (v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_LE
  #define TARGET_NATIVE_MATH_INT_UINT64_LE(v1,v2) ((v1) <= (v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_GT
  #define TARGET_NATIVE_MATH_INT_UINT64_GT(v1,v2) ((v1) >  (v2))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_GE
  #define TARGET_NATIVE_MATH_INT_UINT64_GE(v1,v2) ((v1) >= (v2))
#endif

/* type conversion routines */
#ifndef TARGET_NATIVE_MATH_INT_INT32_TO_INT64
  #define TARGET_NATIVE_MATH_INT_INT32_TO_INT64(v)   ((jlong)(v))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT32_TO_UINT64
  #define TARGET_NATIVE_MATH_INT_UINT32_TO_UINT64(v) ((jlong)(v))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_TO_INT32
  #define TARGET_NATIVE_MATH_INT_INT64_TO_INT32(v)   ((jint)(v))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_TO_UINT32
  #define TARGET_NATIVE_MATH_INT_UINT64_TO_UINT32(v) ((jint)(v))
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_TO_DOUBLE
  #define TARGET_NATIVE_MATH_INT_INT64_TO_DOUBLE(v)  ((jdouble)(v))
#endif

#ifndef TARGET_NATIVE_MATH_INT_INT64_TO_UINT64
  #define TARGET_NATIVE_MATH_INT_INT64_TO_UINT64(v)  ((jlong)(v))
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT64_TO_INT64
  #define TARGET_NATIVE_MATH_INT_UINT64_TO_INT64(v)  ((jlong)(v))
#endif

/* combine/split int32 low/high values <-> int64 values */
#ifndef TARGET_NATIVE_MATH_INT_INT32_LOW_HIGH_TO_INT64
  #define TARGET_NATIVE_MATH_INT_INT32_LOW_HIGH_TO_INT64(low,high,v) \
    do { \
      (v)=((((jlong)(high)) << 32) | ((((jlong)(low)) <<  0) & 0x00000000ffffFFFFLL)); \
    } while (0)
#endif
#ifndef TARGET_NATIVE_MATH_INT_UINT32_LOW_HIGH_TO_UINT64
  #define TARGET_NATIVE_MATH_INT_UINT32_LOW_HIGH_TO_UINT64(low,high,v) \
    do { \
      (v)=((((jlong)(high)) << 32) | ((((jlong)(low)) <<  0) & 0x00000000ffffFFFFLL)); \
    } while (0)
#endif
#ifndef TARGET_NATIVE_MATH_INT_INT64_TO_INT32_LOW_HIGH
  #define TARGET_NATIVE_MATH_INT_INT64_TO_INT32_LOW_HIGH(v,low,high) \
    do { \
      (high)=(jint)(((v) & 0xFFFFffff00000000LL) >> 32); \
      (low) =(jint)(((v) & 0x00000000FFFFffffLL) >>  0); \
    } while (0)
#endif
/* NYI: we need unsigned here, but JNI does not support this */
#ifndef TARGET_NATIVE_MATH_INT_UINT64_TO_UINT32_LOW_HIGH
  #define TARGET_NATIVE_MATH_INT_UINT64_TO_UINT32_LOW_HIGH(v,low,high) \
    do { \
      (high)=(jint)(((v) & 0xFFFFffff00000000LL) >> 32); \
      (low) =(jint)(((v) & 0x00000000FFFFffffLL) >>  0); \
    } while (0)
#endif

/*---------------------------------------------------------------------*/

/* NYI: OPTIMIZATION: it would be nice to check if isnan(), isinf(), finite() is
available on the system, e. g. use HAVE_ISNAN, HAVE_ISINF, HAVE_FINITE
*/

/* test float/double values for NaN,Inf */
#ifndef TARGET_NATIVE_MATH_FLOAT_FLOAT_ISNAN
  #include <math.h>
  #define TARGET_NATIVE_MATH_FLOAT_FLOAT_ISNAN(f) isnan(f)
#endif
#ifndef TARGET_NATIVE_MATH_FLOAT_FLOAT_ISINF
  #include <math.h>
  #define TARGET_NATIVE_MATH_FLOAT_FLOAT_ISINF(f) (isinf(f)!=0)
#endif
#ifndef TARGET_NATIVE_MATH_FLOAT_FLOAT_POS_ISINF
  #include <math.h>
  #define TARGET_NATIVE_MATH_FLOAT_FLOAT_POS_ISINF(f) (isinf(f)>0)
#endif
#ifndef TARGET_NATIVE_MATH_FLOAT_FLOAT_NEG_ISINF
  #include <math.h>
  #define TARGET_NATIVE_MATH_FLOAT_FLOAT_NEG_ISINF(f) (isinf(f)<0)
#endif
#ifndef TARGET_NATIVE_MATH_FLOAT_FLOAT_FINITE
  #include <math.h>
  #define TARGET_NATIVE_MATH_FLOAT_FLOAT_FINITE(f) finite(f)
#endif

#ifndef TARGET_NATIVE_MATH_FLOAT_DOUBLE_ISNAN
  #include <math.h>
  #define TARGET_NATIVE_MATH_FLOAT_DOUBLE_ISNAN(d) isnan(d)
#endif
#ifndef TARGET_NATIVE_MATH_FLOAT_DOUBLE_ISINF
  #include <math.h>
  #define TARGET_NATIVE_MATH_FLOAT_DOUBLE_ISINF(d) (isinf(d)!=0)
#endif
#ifndef TARGET_NATIVE_MATH_FLOAT_DOUBLE_POS_ISINF
  #include <math.h>
  #define TARGET_NATIVE_MATH_FLOAT_DOUBLE_POS_ISINF(d) (isinf(d)>0)
#endif
#ifndef TARGET_NATIVE_MATH_FLOAT_DOUBLE_NEG_ISINF
  #include <math.h>
  #define TARGET_NATIVE_MATH_FLOAT_DOUBLE_NEG_ISINF(d) (isinf(d)<0)
#endif
#ifndef TARGET_NATIVE_MATH_FLOAT_DOUBLE_FINITE
  #include <math.h>
  #define TARGET_NATIVE_MATH_FLOAT_DOUBLE_FINITE(d) finite(d)
#endif

/* division, modulo and multiplication operations (used to avoid unexcepted exceptions on some
   targets; generic codes are direct operations without checks)
*/
#ifndef TARGET_NATIVE_MATH_FLOAT_FLOAT_DIV
  #define TARGET_NATIVE_MATH_FLOAT_FLOAT_DIV(f0,f1) ((f0)/(f1))
#endif
#ifndef TARGET_NATIVE_MATH_FLOAT_FLOAT_MUL
  #define TARGET_NATIVE_MATH_FLOAT_FLOAT_MUL(f0,f1) ((f0)*(f1))
#endif
#ifndef TARGET_NATIVE_MATH_FLOAT_FLOAT_MOD
  #include <math.h>
  #define TARGET_NATIVE_MATH_FLOAT_FLOAT_MOD(f0,f1) ((jfloat)fmod((jdouble)(f0),(jdouble)(f1)))
#endif

#ifndef TARGET_NATIVE_MATH_FLOAT_DOUBLE_DIV
  #define TARGET_NATIVE_MATH_FLOAT_DOUBLE_DIV(d0,d1) ((d0)/(d1))
#endif
#ifndef TARGET_NATIVE_MATH_FLOAT_DOUBLE_MUL
  #define TARGET_NATIVE_MATH_FLOAT_DOUBLE_MUL(d0,d1) ((d0)*(d1))
#endif
#ifndef TARGET_NATIVE_MATH_FLOAT_DOUBLE_MOD
  #include <math.h>
  #define TARGET_NATIVE_MATH_FLOAT_DOUBLE_MOD(d0,d1) fmod(d0,d1)
#endif

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_GENERIC_MATH__ */

/* end of file */

