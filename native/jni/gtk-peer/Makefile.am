pkglib_LTLIBRARIES = libgtkpeer.la

libgtkpeer_la_SOURCES = \
			gnu_java_awt_peer_gtk_GdkFontMetrics.c \
			gnu_java_awt_peer_gtk_GtkFileDialogPeer.c \
			gnu_java_awt_peer_gtk_GtkScrollPanePeer.c \
			gnu_java_awt_peer_gtk_GdkGraphics.c \
			gnu_java_awt_peer_gtk_GtkImagePainter.c \
			gnu_java_awt_peer_gtk_GtkTextAreaPeer.c \
			gthread-jni.c \
			gnu_java_awt_peer_gtk_GtkLabelPeer.c \
			gnu_java_awt_peer_gtk_GtkTextComponentPeer.c \
			gnu_java_awt_peer_gtk_GtkButtonPeer.c \
			gnu_java_awt_peer_gtk_GtkListPeer.c	\
			gnu_java_awt_peer_gtk_GtkTextFieldPeer.c \
			gnu_java_awt_peer_gtk_GtkCanvasPeer.c \
			gnu_java_awt_peer_gtk_GtkMainThread.c \
			gnu_java_awt_peer_gtk_GtkToolkit.c \
			gnu_java_awt_peer_gtk_GtkCheckboxMenuItemPeer.c \
			gnu_java_awt_peer_gtk_GtkMenuBarPeer.c \
			gnu_java_awt_peer_gtk_GtkWindowPeer.c \
			gnu_java_awt_peer_gtk_GtkCheckboxPeer.c \
			gnu_java_awt_peer_gtk_GtkMenuItemPeer.c \
			gnu_java_awt_peer_gtk_GtkChoicePeer.c \
			gnu_java_awt_peer_gtk_GtkMenuPeer.c \
			gnu_java_awt_peer_gtk_GtkClipboard.c \
			gnu_java_awt_peer_gtk_GtkPanelPeer.c \
			gnu_java_awt_peer_gtk_GtkComponentPeer.c \
			gnu_java_awt_peer_gtk_GtkPopupMenuPeer.c \
			gnu_java_awt_peer_gtk_GtkEvents.c \
			gnu_java_awt_peer_gtk_GtkScrollBarPeer.c \
			gthread-jni.h \
			gtkpeer.h 

libgtkpeer_la_LIBADD = $(top_builddir)/native/jni/classpath/native_state.lo
libgtkpeer_la_LDFLAGS = -module -version-info @LIBVERSION@ @GTK_LIBS@ @GLIB_LIBS@ @LIBART_LIBS@

INCLUDES = -I. @EXTRA_INCLUDES@ \
	   @GTK_CFLAGS@ \
	   @LIBART_CFLAGS@ \
	   -I$(top_srcdir)/native/jni/classpath \
           -I$(top_srcdir)/include \
	   -I$(top_builddir)/include

