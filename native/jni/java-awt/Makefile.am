pkglib_LTLIBRARIES = libjavaawt.la

libjavaawt_la_SOURCES = gnu_java_awt_EmbeddedWindow.c
			

libjavaawt_la_LDFLAGS = -module -version-info @LIBVERSION@
libjavaawt_la_LIBADD = $(top_builddir)/native/jni/classpath/jcl.lo

INCLUDES = -I. @EXTRA_INCLUDES@ \
	   -I$(top_srcdir)/native/jni/classpath \
           -I$(top_srcdir)/include \
	   -I$(top_builddir)/include 
