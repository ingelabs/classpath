lib_LTLIBRARIES = libgtkpeer.la

libgtkpeer_la_SOURCES = \
gdkgraphics.c	    gtkscrollpanepeer.c \
gthread-jni.c       gtkevents.c          gtktextareapeer.c \
gtkbuttonpeer.c     gtkfiledialogpeer.c  gtktextcomponentpeer.c \
gtkcanvaspeer.c     gtkimagepainter.c    gtktextfieldpeer.c \
gtkcheckboxpeer.c   gtklabelpeer.c       gtktoolkit.c \
gtkchoicepeer.c     gtklistpeer.c        gtkwindowpeer.c \
gtkclipboard.c      gtkmainthread.c      native_state.c \
gtkcomponentpeer.c  gtkpanelpeer.c \
gtkcontainerpeer.c  gtkscrollbarpeer.c

libgtkpeer_la_LDFLAGS = -version-info 0:0:0 @GTK_LIBS@ @GLIB_LIBS@

INCLUDES += @JAPHAR_CFLAGS@ \
	    @GTK_CFLAGS@ \
	    -I$(top_srcdir)/include
