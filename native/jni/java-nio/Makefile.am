pkglib_LTLIBRARIES = libjavanio.la

libjavanio_la_SOURCES = gnu_java_nio_NIOServerSocket.c \
			gnu_java_nio_VMPipe.c \
			gnu_java_nio_VMSelector.c \
			gnu_java_nio_channels_FileChannelImpl.c \
			java_nio_DirectByteBufferImpl.c \
			java_nio_MappedByteBufferImpl.c \
			java_nio_channels_Channels.c

libjavanio_la_LIBADD = $(top_builddir)/native/jni/classpath/jcl.lo

AM_LDFLAGS = @CLASSPATH_MODULE@
AM_CPPFLAGS = @CLASSPATH_INCLUDES@
AM_CFLAGS = @WARNING_CFLAGS@ @STRICT_WARNING_CFLAGS@ @ERROR_CFLAGS@
