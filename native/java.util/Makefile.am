pkglib_LTLIBRARIES = libjavautil.la

libjavautil_la_SOURCES = TimeZone.c \
                         ResourceBundle.c 

libjavautil_la_LDFLAGS = -version-info @LIBVERSION@

INCLUDES +=  @EXTRA_INCLUDES@ -I$(top_srcdir)/include

if JAPHAR
install-data-local : 
	-cd $(pkglibdir) && ln -s libjavautil.so libjaphar_javautil.so
endif

clean-local:
	-rm -rf .deps

