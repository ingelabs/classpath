## Input file for automake to generate the Makefile.in used by configure

if JAPHAR
lib_LTLIBRARIES = libjaphar_javanet.la

libjaphar_javanet_la_SOURCES = InetAddress.c \
                               PlainDatagramSocketImpl.c \
                               PlainSocketImpl.c \
                               javanet.c \
                               javanet.h \
                               java_net_InetAddress.h \
                               java_net_PlainSocketImpl.h \
                               java_net_PlainDatagramSocketImpl.h 

else
lib_LTLIBRARIES = libjavanet.la

libjavanet_la_SOURCES = InetAddress.c PlainDatagramSocketImpl.c \
                        PlainSocketImpl.c javanet.c javanet.h 

endif

INCLUDES += @JAPHAR_CFLAGS@

libjaphar_javanet_la_LDFLAGS = -version-info 0:0:0
libjavanet_la_LDFLAGS = -version-info 0:0:0

