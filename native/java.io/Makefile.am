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

libjaphar_javaio_la_LDFLAGS = -version-info 0:0:0 
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

libjavaio_la_LDFLAGS = -version-info 0:0:0 
endif

if JAPHAR
INCLUDES +=  @JAPHAR_CFLAGS@
endif


#if JAPHAR
#JAVAHEADER = @JAPHAR_JAVAH@ --classpath $(top_srcdir)/lib --header
#endif
#
#File.c: java_io_File.h
#FileDescriptor.c: java_io_FileDescriptor.h
#FileInputStream.c: java_io_FileInputStream.h
#FileOutputStream.c: java_io_FileOutputStream.h
#
## How to build JNI *.h files
#
#java_io_File.h:
#	$(JAVAHEADER) --d $(srcdir) java.io.File
#
#java_io_FileDescriptor.h:
#	$(JAVAHEADER) --d $(srcdir) java.io.FileDescriptor
#
#java_io_FileInputStream.h:
#	$(JAVAHEADER) --d $(srcdir) java.io.FileInputStream
#
#java_io_FileOutputStream.h:
#	$(JAVAHEADER) --d $(srcdir) java.io.FileOutputStream
#
