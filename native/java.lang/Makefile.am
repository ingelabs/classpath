
if JAPHAR
lib_LTLIBRARIES = libjaphar_javalang.la libjaphar_java_lang_Math.la

libjaphar_javalang_la_SOURCES = System.c Object.c System.h Object.h \
Float.c java_lang_Float.h javalang.c javalang.h $(top_srcdir)/config.h

libjaphar_javalang_la_LDFLAGS = -version-info 0:0:0

libjaphar_java_lang_Math_la_SOURCES = acos.c asin.c atan.c atan2.c ceil.c \
cos.c e_fmod.c exp.c floor.c k_cos.c k_rem_pio2.c k_sin.c k_tan.c \
log.c pow.c remainder.c rint.c s_fabs.c sin.c sqrt.c tan.c fdlibm.h \
java_lang_Math.h

libjaphar_java_lang_Math_la_LDFLAGS = -version-info 0:0:0
else
lib_LTLIBRARIES = libjavalang.la libjava_lang_Math.la

libjavalang_la_SOURCES = System.c Object.c Float.c java_lang_Float.h\
javalang.c javalang.h $(top_srcdir)/config.h

libjavalang_la_LDFLAGS = -version-info 0:0:0

libjava_lang_Math_la_SOURCES = acos.c asin.c atan.c atan2.c ceil.c \
cos.c e_fmod.c exp.c floor.c k_cos.c k_rem_pio2.c k_sin.c k_tan.c \
log.c pow.c remainder.c rint.c s_fabs.c sin.c sqrt.c tan.c

libjava_lang_Math_la_LDFLAGS = -version-info 0:0:0
endif

if JAPHAR
INCLUDES += @JAPHAR_CFLAGS@
endif

INCLUDES += -I$(top_srcdir)
