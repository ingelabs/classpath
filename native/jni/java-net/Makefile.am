pkglib_LTLIBRARIES = libjavanet.la

libjavanet_la_SOURCES = javanet.c \
			javanet.h \
			java_net_InetAddress.c \
			java_net_NetworkInterface.c \
			gnu_java_net_PlainDatagramSocketImpl.c \
                        gnu_java_net_PlainSocketImpl.c

libjavanet_la_LDFLAGS = @CLASSPATH_MODULE@
libjavanet_la_LIBADD = $(top_builddir)/native/jni/classpath/jcl.lo
