pkglib_LTLIBRARIES = libcpgdkpixbuf.la

libcpgdkpixbuf_la_SOURCES = \
gdkpixbufdecoder.c

libcpgdkpixbuf_la_LDFLAGS = -version-info @LIBVERSION@ $(top_builddir)/native/gnu.java.awt.peer.gtk/libgtkpeer.la @GTK_LIBS@ @GLIB_LIBS@ @LIBART_LIBS@ -lgdk_pixbuf

INCLUDES += @EXTRA_INCLUDES@ \
	    @GTK_CFLAGS@ \
	    @LIBART_CFLAGS@ \
	    -I$(top_srcdir)/include

clean-local:
	-rm -rf .deps

