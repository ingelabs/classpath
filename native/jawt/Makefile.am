pkglib_LTLIBRARIES = libjawtgnu.la

libjawtgnu_la_SOURCES = jawt.c
libjawtgnu_la_LIBADD = $(top_builddir)/native/jni/gtk-peer/libgtkpeer.la

AM_LDFLAGS = @CLASSPATH_MODULE@ @GTK_LIBS@ @CAIRO_LIBS@ @PANGOFT2_LIBS@ @X_LIBS@ -lXtst
AM_CPPFLAGS = @CLASSPATH_INCLUDES@

# Just the WARNING_CFLAGS. We cannot use the strict flags since the gtk
# headers contain broken prototypes (by design, see gtkitemfactory.h).
AM_CFLAGS = @WARNING_CFLAGS@ @ERROR_CFLAGS@ \
            @GTK_CFLAGS@ @CAIRO_CFLAGS@ @PANGOFT2_CFLAGS@
