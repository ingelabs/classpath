pkglib_LTLIBRARIES = libjavanio.la

libjavanio_la_SOURCES = gnu_java_nio_NIOServerSocket.c \
			gnu_java_nio_PipeImpl.c \
			gnu_java_nio_SelectorImpl.c \
			gnu_java_nio_channels_FileChannelImpl.c \
			java_nio_DirectByteBufferImpl.c \
			java_nio_MappedByteBufferImpl.c

libjavanio_la_LDFLAGS = @CLASSPATH_MODULE@
libjavanio_la_LIBADD = $(top_builddir)/native/jni/classpath/jcl.lo

INCLUDES = -I. @EXTRA_INCLUDES@ \
	   -I$(top_srcdir)/native/jni/classpath \
           -I$(top_srcdir)/include \
	   -I$(top_builddir)/include 
