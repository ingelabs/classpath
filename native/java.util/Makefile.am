pkglib_LTLIBRARIES = libjavautil.la

libjavautil_la_SOURCES = TimeZone.c \
                         ResourceBundle.c 

libjavautil_la_LDFLAGS = -version-info @LIBVERSION@

INCLUDES +=  @EXTRA_INCLUDES@ -I$(top_srcdir)/include

clean-local:
	-rm -rf .deps

