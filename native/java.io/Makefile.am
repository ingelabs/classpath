pkglib_LTLIBRARIES = libjavaio.la

libjavaio_la_SOURCES = File.c FileDescriptor.c FileInputStream.c \
                       FileOutputStream.c javaio.c javaio.h RandomAccessFile.c \
                       java_io_ObjectOutputStream.c \
                       java_io_ObjectInputStream.c

libjavaio_la_LDFLAGS = -version-info @LIBVERSION@ 

INCLUDES +=  @EXTRA_INCLUDES@ \
	     -I$(top_srcdir)/include

if JAPHAR
install-data-local : 
	-cd $(pkglibdir) && ln -s libjavaio.so libjaphar_javaio.so
endif

clean-local:
	-rm -rf .deps

