## Input file for automake to generate the Makefile.in used by configure

lib_LTLIBRARIES = libjaphar_javanet.la

libjaphar_javanet_la_SOURCES = InetAddress.c PlainDatagramSocketImpl.c \
                               PlainSocketImpl.c javanet.c javanet.h 

libjaphar_javanet_la_LDFLAGS = -version-info 0:0:0

if JAPHAR
INCLUDES += @JAPHAR_CFLAGS@
endif

#InetAddress.c: java_net_InetAddress.h

#PlainDatagramSocketImpl.c: java_net_PlainDatagramSocketImpl.h

#PlainSocketImpl.c: java_net_PlainSocketImpl.h 

# How to build stubs
# How to build JNI *.h files

java_net_InetAddress.h:
	javah -jni -d $(srcdir) java.net.InetAddress

java_net_PlainDatagramSocketImpl.h:
	javah -jni -d $(srcdir) java.net.PlainDatagramSocketImpl

java_net_PlainSocketImpl.h:
	javah -jni -d $(srcdir) java.net.PlainSocketImpl

