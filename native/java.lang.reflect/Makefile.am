pkglib_LTLIBRARIES = libjavalangreflect.la

libjavalangreflect_la_SOURCES = Array.c

libjavalangreflect_la_LDFLAGS = -version-info @LIBVERSION@

INCLUDES += -I ../lib -I ../vmi -I ../nsa -DNO_VMI @EXTRA_INCLUDES@ \
	    -I$(top_srcdir)/include

clean-local:
	-rm -rf .deps

