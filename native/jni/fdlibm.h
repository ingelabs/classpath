#include <config.h>

#ifndef __FDLIBM__
#define __FDLIBM__

/* @(#)fdlibm.h 1.5 95/01/18 */
/*
 * ====================================================
 * Copyright (C) 1993 by Sun Microsystems, Inc. All rights reserved.
 * Portions Copyright (c) 1998 Free Software Foundation, Inc.
 *
 * Developed at SunSoft, a Sun Microsystems, Inc. business.
 * Permission to use, copy, modify, and distribute this
 * software is freely granted, provided that this notice 
 * is preserved.
 * ====================================================
 */

#include "java_lang_Math.h"
#include <stdlib.h>

#define sqrt(x) Java_java_lang_Math_sqrt(NULL, NULL, x)

#ifndef WORDS_BIGENDIAN
#define __HI(x) *(1+(jint*)&x)
#define __LO(x) *(jint*)&x
#define __HIp(x) *(1+(jint*)x)
#define __LOp(x) *(jint*)x
#else
#define __HI(x) *(jint*)&x
#define __LO(x) *(1+(jint*)&x)
#define __HIp(x) *(jint*)x
#define __LOp(x) *(1+(jint*)x)
#endif

#define	MAXFLOAT	((float)3.40282346638528860e+38)

enum fdversion {fdlibm_ieee = -1, fdlibm_svid, fdlibm_xopen, fdlibm_posix};

#define _LIB_VERSION_TYPE enum fdversion
#define _LIB_VERSION _fdlib_version  

/* if global variable _LIB_VERSION is not desirable, one may 
 * change the following to be a constant by: 
 *	#define _LIB_VERSION_TYPE const enum version
 * In that case, after one initializes the value _LIB_VERSION (see
 * s_lib_version.c) during compile time, it cannot be modified
 * in the middle of a program
 */ 
extern  _LIB_VERSION_TYPE  _LIB_VERSION;

#define _IEEE_  fdlibm_ieee
#define _SVID_  fdlibm_svid
#define _XOPEN_ fdlibm_xopen
#define _POSIX_ fdlibm_posix

struct exception {
  int type;
  char *name;
  double arg1;
  double arg2;
  double retval;
};

#define	HUGE		MAXFLOAT

#define	DOMAIN		1
#define	SING		2
#define	OVERFLOW	3
#define	UNDERFLOW	4
#define	TLOSS		5
#define	PLOSS		6

/* ieee style elementary functions */
extern jdouble __ieee754_fmod (jdouble,jdouble);
extern jdouble fabs (jdouble);

/* fdlibm kernel function */
extern jdouble __kernel_sin (jdouble, jdouble, jint);
extern jdouble __kernel_cos (jdouble, jdouble);
extern jdouble __kernel_tan (jdouble, jdouble, jint);
extern jint   __kernel_rem_pio2 (jdouble*,jdouble*,jint,jint,jint,const jint*);
#endif
