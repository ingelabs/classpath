pkglib_LTLIBRARIES = libclasspath.la

libclasspath_la_SOURCES = jcl.c jcl.h jnilink.c jnilink.h primlib.c primlib.h

libclasspath_la_LDFLAGS = -version-info @LIBVERSION@

INCLUDES += -I../lib -I../nsa -I../vmi -I. -DNO_VMI @EXTRA_INCLUDES@ \
	    -I$(top_srcdir)/include

clean-local:
	-rm -rf .deps

