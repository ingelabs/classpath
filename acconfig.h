/***********************************************************************
 * This file is included by autoheader to create config.h.in which is
 * used by configure to create config.h.  We include $(top_srcdir) as
 * an INCLUDE in every native Makefile.am and then use the values from
 * the defines to structure our program for a native platform.
 * In this file, only include things autoheader cannot determine for 
 * itself, for example when using AC_DEFINE.
 *
 * Leave the blank line below, required by autoheader.
 ***********************************************************************/

/* Define as 1 if building for Japhar */
#define WITH_JAPHAR 0

/* Define as 1 if building for Kaffe */
#define WITH_KAFFE 0

/* #undef PACKAGE */
/* #undef VERSION */

/* Define as 1 if building with debugging code included, this is not -g */
#define DEBUG 0

/* Define if you have int32_t and uint32_t */
#undef HAVE_INT32_DEFINED

/* Define if you have u_int32_t */
#undef HAVE_BSD_INT32_DEFINED

/************************************************************************
 * Leave the blank line there, required by autoheader.
 ************************************************************************/
