pkglib_LTLIBRARIES = libjavaio.la 

libjavaio_la_SOURCES = 	javaio.h \
			javaio.c \
			java_io_File.c \
			FileDescriptor.c \
			java_io_ObjectInputStream.c \
			java_nio.c


libjavaio_la_LDFLAGS = -module -version-info @LIBVERSION@ 

libjavaio_la_LIBADD = $(top_builddir)/native/jni/classpath/jcl.lo
INCLUDES = -I. @EXTRA_INCLUDES@ \
	   -I$(top_srcdir)/native/jni/classpath \
           -I$(top_srcdir)/include \
	   -I$(top_builddir)/include 
