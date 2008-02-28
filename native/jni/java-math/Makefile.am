nativeexeclib_LTLIBRARIES = libjavamath.la

libjavamath_la_SOURCES = java_math_VMBigInteger.c

libjavamath_la_LIBADD = $(top_builddir)/native/jni/classpath/jcl.lo

libjavamath_la_LDFLAGS = -avoid-version

AM_LDFLAGS = @CLASSPATH_MODULE@ @GMP_LIBS@
AM_CPPFLAGS = @CLASSPATH_INCLUDES@
AM_CFLAGS = @WARNING_CFLAGS@ @ERROR_CFLAGS@ @GMP_CFLAGS@
