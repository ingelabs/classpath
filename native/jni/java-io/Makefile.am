SUBDIRS = 

pkglib_LTLIBRARIES = libjavaio.la 

libjavaio_la_SOURCES = 	javaio.h \
			javaio.c \
			java_io_File.c \
			java_io_FileDescriptor.c \
			java_io_FileInputStream.c \
			java_io_FileOutputStream.c \
			java_io_RandomAccessFile.c \
			java_io_ObjectOutputStream.c \
			java_io_ObjectInputStream.c

libjavaio_la_LDFLAGS = -module -version-info @LIBVERSION@ 

libjavaio_la_LIBADD = $(top_builddir)/native/jni/classpath/jcl.lo
INCLUDES = @EXTRA_INCLUDES@ \
	   -I$(top_srcdir)/native/jni/classpath \
	   -I$(top_builddir)/include 

