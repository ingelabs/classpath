pkglib_LTLIBRARIES = libclasspath.la

libclasspath_la_SOURCES = jcl.c jcl.h jnilink.c jnilink.h primlib.c primlib.h

libclasspath_la_LDFLAGS = -version-info @LIBVERSION@

INCLUDES += -I../lib -I../nsa -I../vmi -I. -DNO_VMI @EXTRA_INCLUDES@ \
	    -I$(top_srcdir)/include

if JAPHAR
install-data-local : 
	-cd $(pkglibdir) && ln -s libclasspath.so libjaphar_classpath.so
endif

clean-local:
	-rm -rf .deps

