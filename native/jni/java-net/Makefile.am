pkglib_LTLIBRARIES = libjavanet.la

libjavanet_la_SOURCES = javanet.c \
			javanet.h \
			java_net_InetAddress.c \
			java_net_NetworkInterface.c \
			java_net_PlainDatagramSocketImpl.c \
                        java_net_PlainSocketImpl.c

libjavanet_la_LDFLAGS = -module -version-info @LIBVERSION@
libjavanet_la_LIBADD = $(top_builddir)/native/jni/classpath/jcl.lo

INCLUDES = -I. @EXTRA_INCLUDES@ \
	   -I$(top_srcdir)/native/jni/classpath \
           -I$(top_srcdir)/include \
	   -I$(top_builddir)/include 
