lib_LTLIBRARIES = libcpgdkpixbuf.la

libcpgdkpixbuf_la_SOURCES = \
gdkpixbufdecoder.c

libcpgdkpixbuf_la_LDFLAGS = -version-info 0:0:0 -L../gnu.java.awt.peer.gtk/.libs @GTK_LIBS@ @GLIB_LIBS@ @LIBART_LIBS@ -lgdk_pixbuf -lgnomeui -lgnome -lpopt -lgtkpeer

INCLUDES += @JAPHAR_CFLAGS@ \
	    @GTK_CFLAGS@ \
	    @LIBART_CFLAGS@ \
	    -I$(top_srcdir)/include
