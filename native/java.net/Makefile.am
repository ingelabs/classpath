## Input file for automake to generate the Makefile.in used by configure

pkglib_LTLIBRARIES = libjavanet.la

libjavanet_la_SOURCES = InetAddress.c PlainDatagramSocketImpl.c \
                        PlainSocketImpl.c javanet.c javanet.h 

INCLUDES += @EXTRA_INCLUDES@ \
	    -I$(top_srcdir)/include

libjavanet_la_LDFLAGS = -version-info @LIBVERSION@

if JAPHAR
install-data-local : 
	cd $(pkglibdir) && ln -s libjavanet.so libjaphar_javanet.so
endif

clean-local:
	-rm -rf .deps

