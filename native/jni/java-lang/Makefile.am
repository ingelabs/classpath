pkglib_LTLIBRARIES = 	libjavalang.la 	libjavalangreflect.la

libjavalang_la_SOURCES = java_lang_VMSystem.c \
			java_lang_Object.c \
			java_lang_Float.c \
			java_lang_Double.c \
			java_lang_Math.c

libjavalangreflect_la_SOURCES = java_lang_reflect_Array.c

libjavalang_la_LIBADD = $(wildcard $(top_builddir)/native/fdlibm/*.lo) \
			$(top_builddir)/native/jni/classpath/jcl.lo
libjavalang_la_LDFLAGS = -module -version-info @LIBVERSION@
libjavalangreflect_la_LDFLAGS = -module -version-info @LIBVERSION@

INCLUDES = -I. @EXTRA_INCLUDES@ \
	   -I$(top_srcdir)/native/jni/classpath \
	   -I$(top_srcdir)/native/fdlibm \
           -I$(top_srcdir)/include \
	   -I$(top_builddir)/include 

