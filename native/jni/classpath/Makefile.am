noinst_LTLIBRARIES = libclasspath.la

libclasspath_la_SOURCES = 	jcl.c jcl.h \
				jnilink.c jnilink.h \
				primlib.c primlib.h \
				native_state.c native_state.h

libclasspath_la_LDFLAGS = -module -version-info @LIBVERSION@

INCLUDES =  -I. @EXTRA_INCLUDES@ \
	    -I$(top_srcdir)/include \
	    -I$(top_builddir)/include 

