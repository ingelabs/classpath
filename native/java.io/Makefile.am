if JAPHAR
lib_LTLIBRARIES = libjaphar_javaio.la

libjaphar_javaio_la_SOURCES = File.c FileDescriptor.c FileInputStream.c \
                       FileOutputStream.c javaio.c javaio.h RandomAccessFile.c \
                       java_io_ObjectOutputStream.c \
                       java_io_ObjectInputStream.c \
                       java_io_ObjectStreamClass.c \
                       java_io_File.h \
                       java_io_FileInputStream.h \
                       java_io_FileOutputStream.h \
                       java_io_RandomAccessFile.h \
                       java_io_ObjectInputStream.h \
                       java_io_ObjectOutputStream.h \
                       java_io_ObjectStreamClass.h

else
lib_LTLIBRARIES = libjavaio.la

libjavaio_la_SOURCES = File.c FileDescriptor.c FileInputStream.c \
                       FileOutputStream.c javaio.c RandomAccessFile.c \
                       java_io_ObjectOutputStream.c \
                       java_io_ObjectInputStream.c \
                       java_io_ObjectStreamClass.c \
                       java_io_File.h \
                       java_io_FileInputStream.h \
                       java_io_FileOutputStream.h \
                       java_io_RandomAccessFile.h \
                       java_io_ObjectInputStream.h \
                       java_io_ObjectOutputStream.h \
                       java_io_ObjectStreamClass.h

endif

libjaphar_javaio_la_LDFLAGS = -version-info 0:0:0 
libjavaio_la_LDFLAGS = -version-info 0:0:0 

INCLUDES +=  @JAPHAR_CFLAGS@ \
	     -I$(top_srcdir)/include
