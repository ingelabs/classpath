pkglib_LTLIBRARIES = libgtkpeer.la

libgtkpeer_la_SOURCES = \
gdkfontmetrics.c	   gtkfiledialogpeer.c	gtkscrollpanepeer.c \
gdkgraphics.c		   gtkimagepainter.c	gtktextareapeer.c \
gthread-jni.c		   gtklabelpeer.c	gtktextcomponentpeer.c \
gtkbuttonpeer.c		   gtklistpeer.c	gtktextfieldpeer.c \
gtkcanvaspeer.c		   gtkmainthread.c	gtktoolkit.c \
gtkcheckboxmenuitempeer.c  gtkmenubarpeer.c	gtkwindowpeer.c \
gtkcheckboxpeer.c	   gtkmenuitempeer.c	native_state.c \
gtkchoicepeer.c		   gtkmenupeer.c \
gtkclipboard.c		   gtkpanelpeer.c \
gtkcomponentpeer.c	   gtkpopupmenupeer.c \
gtkevents.c		   gtkscrollbarpeer.c

libgtkpeer_la_LDFLAGS = -version-info @LIBVERSION@ @GTK_LIBS@ @GLIB_LIBS@ @LIBART_LIBS@

INCLUDES += @EXTRA_INCLUDES@ \
	    @GTK_CFLAGS@ \
	    @LIBART_CFLAGS@ \
	    -I$(top_srcdir)/include

if JAPHAR
install-data-local : 
	-cd $(pkglibdir) && ln -s libgtkpeer.so libjaphar_gtkpeer.so
endif

clean-local:
	-rm -rf .deps

