pkglib_LTLIBRARIES = libjavautil.la

libjavautil_la_SOURCES = java_util_TimeZone.c

libjavautil_la_LDFLAGS = -module -version-info @LIBVERSION@

INCLUDES = -I. @EXTRA_INCLUDES@ \
           -I$(top_srcdir)/include \
	   -I$(top_builddir)/include 
