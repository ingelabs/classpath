lib_LTLIBRARIES = libjavalang.la libjavalangmath.la

libjavalang_la_SOURCES = System.c Object.c Float.c \
javalang.c javalang.h Double.c 

libjavalangmath_la_SOURCES = acos.c asin.c atan.c atan2.c ceil.c \
cos.c e_fmod.c exp.c floor.c k_cos.c k_rem_pio2.c k_sin.c k_tan.c \
log.c pow.c remainder.c rint.c s_fabs.c sin.c sqrt.c tan.c fdlibm.h

libjavalang_la_LDFLAGS = -version-info @LIBVERSION@
libjavalangmath_la_LDFLAGS = -version-info @LIBVERSION@

INCLUDES += @EXTRA_INCLUDES@ \
            -I$(top_srcdir)/include
