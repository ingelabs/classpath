
lib_LTLIBRARIES = libjava_io_ObjectOutputStream.la libjava_io_ObjectStreamClass.la libjava_io_ObjectInputStream.la libjavaio.la

libjava_io_ObjectOutputStream_la_SOURCES = java_io_ObjectOutputStream.c

libjava_io_ObjectOutputStream_la_LDFLAGS = -version-info 0:0:0

libjava_io_ObjectStreamClass_la_SOURCES = java_io_ObjectStreamClass.c

libjava_io_ObjectStreamClass_la_LDFLAGS = -version-info 0:0:0

libjava_io_ObjectInputStream_la_SOURCES = java_io_ObjectInputStream.c

libjava_io_ObjectInputStream_la_LDFLAGS = -version-info 0:0:0

libjavaio_la_SOURCES = File.c FileDescriptor.c FileInputStream.c FileOutputStream.c javaio.c

libjavaio_la_LDFLAGS = -version-info 0:0:0

INCLUDES += -I ../lib @VMINCLUDE@

#File.c: java_io_File.h
#FileDescriptor.c: java_io_FileDescriptor.h
#FileInputStream.c: java_io_FileInputStream.h
#FileOutputStream.c: java_io_FileOutputStream.h

# How to build JNI *.h files

#java_io_File.h:
#	javah -jni -d $(srcdir) java.io.File

#java_io_FileDescriptor.h:
#	javah -jni -d $(srcdir) java.io.FileDescriptor

#java_io_FileInputStream.h:
#	javah -jni -d $(srcdir) java.io.FileInputStream

#java_io_FileOutputStream.h:
#	javah -jni -d $(srcdir) java.io.FileOutputStream
