DISTCLEANFILES = jni_md.h

ARG_JNI_JAVAH = -jni
ARG_CLASSPATH_JAVAH = -bootclasspath
JAVAH = $(USER_JAVAH) $(ARG_JNI_JAVAH) $(ARG_CLASSPATH_JAVAH) ../lib:$(USER_CLASSLIB)

H_FILES = \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkFontMetrics.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkFontPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkGlyphVector.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkGraphics.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkGraphics2D.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkGraphicsEnvironment.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkPixbufDecoder.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkTextLayout.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkButtonPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkCanvasPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkCheckboxGroupPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkCheckboxMenuItemPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkCheckboxPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkChoicePeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkClipboard.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkComponentPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkEmbeddedWindowPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkFileDialogPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkFramePeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkGenericPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkImagePainter.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkLabelPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkListPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkMainThread.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkMenuBarPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkMenuComponentPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkMenuItemPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkMenuPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkPanelPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkPopupMenuPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkScrollPanePeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkScrollbarPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkTextAreaPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkTextComponentPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkTextFieldPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkToolkit.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkWindowPeer.h \
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GThreadNativeMethodRunner.h \
$(top_srcdir)/include/gnu_java_net_PlainDatagramSocketImpl.h \
$(top_srcdir)/include/gnu_java_net_PlainSocketImpl.h \
$(top_srcdir)/include/gnu_java_nio_VMPipe.h \
$(top_srcdir)/include/gnu_java_nio_VMSelector.h \
$(top_srcdir)/include/gnu_java_nio_channels_FileChannelImpl.h \
$(top_srcdir)/include/java_io_ObjectInputStream.h \
$(top_srcdir)/include/java_io_VMFile.h \
$(top_srcdir)/include/java_io_VMObjectStreamClass.h \
$(top_srcdir)/include/java_lang_Double.h \
$(top_srcdir)/include/java_lang_Math.h \
$(top_srcdir)/include/java_lang_Object.h \
$(top_srcdir)/include/java_lang_VMDouble.h \
$(top_srcdir)/include/java_lang_VMFloat.h \
$(top_srcdir)/include/java_lang_VMProcess.h \
$(top_srcdir)/include/java_lang_VMSystem.h \
$(top_srcdir)/include/java_lang_reflect_Array.h \
$(top_srcdir)/include/java_net_InetAddress.h \
$(top_srcdir)/include/java_net_NetworkInterface.h \
$(top_srcdir)/include/java_nio_VMDirectByteBuffer.h \
$(top_srcdir)/include/java_nio_MappedByteBufferImpl.h \
$(top_srcdir)/include/java_nio_channels_Channels.h \
$(top_srcdir)/include/java_util_VMTimeZone.h

if CREATE_JNI_HEADERS

all-local: $(H_FILES)

$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkFontMetrics.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GdkFontMetrics.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GdkFontMetrics
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkFontPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GdkFontPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GdkFontPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkGlyphVector.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GdkGlyphVector.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GdkGlyphVector
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkGraphics.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GdkGraphics.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GdkGraphics
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkGraphics2D.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GdkGraphics2D.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GdkGraphics2D
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkGraphicsEnvironment.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GdkGraphicsEnvironment.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GdkGraphicsEnvironment
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkPixbufDecoder.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GdkPixbufDecoder.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GdkPixbufDecoder
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GdkTextLayout.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GdkTextLayout.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GdkTextLayout
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkButtonPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkButtonPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkButtonPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkCanvasPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkCanvasPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkCanvasPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkCheckboxGroupPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkCheckboxGroupPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkCheckboxGroupPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkCheckboxMenuItemPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkCheckboxMenuItemPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkCheckboxMenuItemPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkCheckboxPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkCheckboxPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkCheckboxPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkChoicePeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkChoicePeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkChoicePeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkClipboard.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkClipboard.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkClipboard
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkComponentPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkComponentPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkComponentPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkEmbeddedWindowPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkEmbeddedWindowPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkEmbeddedWindowPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkFileDialogPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkFileDialogPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkFileDialogPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkFramePeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkFramePeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkFramePeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkGenericPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkGenericPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkGenericPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkImagePainter.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkImagePainter.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkImagePainter
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkLabelPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkLabelPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkLabelPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkListPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkListPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkListPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkMainThread.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkMainThread.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkMainThread
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkMenuBarPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkMenuBarPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkMenuBarPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkMenuComponentPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkMenuComponentPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkMenuComponentPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkMenuItemPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkMenuItemPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkMenuItemPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkMenuPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkMenuPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkMenuPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkPanelPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkPanelPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkPanelPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkPopupMenuPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkPopupMenuPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkPopupMenuPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkScrollPanePeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkScrollPanePeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkScrollPanePeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkScrollbarPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkScrollbarPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkScrollbarPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkTextAreaPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkTextAreaPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkTextAreaPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkTextComponentPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkTextComponentPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkTextComponentPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkTextFieldPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkTextFieldPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkTextFieldPeer
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkToolkit.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkToolkit.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkToolkit
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GThreadNativeMethodRunner.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GThreadNativeMethodRunner.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GThreadNativeMethodRunner
$(top_srcdir)/include/gnu_java_awt_peer_gtk_GtkWindowPeer.h: $(top_srcdir)/gnu/java/awt/peer/gtk/GtkWindowPeer.java
	$(JAVAH) -o $@ gnu.java.awt.peer.gtk.GtkWindowPeer
$(top_srcdir)/include/gnu_java_net_PlainDatagramSocketImpl.h: $(top_srcdir)/gnu/java/net/PlainDatagramSocketImpl.java
	$(JAVAH) -o $@ gnu.java.net.PlainDatagramSocketImpl
$(top_srcdir)/include/gnu_java_net_PlainSocketImpl.h: $(top_srcdir)/gnu/java/net/PlainSocketImpl.java
	$(JAVAH) -o $@ gnu.java.net.PlainSocketImpl
$(top_srcdir)/include/gnu_java_nio_VMPipe.h: $(top_srcdir)/vm/reference/gnu/java/nio/VMPipe.java
	$(JAVAH) -o $@ gnu.java.nio.VMPipe
$(top_srcdir)/include/gnu_java_nio_VMSelector.h: $(top_srcdir)/vm/reference/gnu/java/nio/VMSelector.java
	$(JAVAH) -o $@ gnu.java.nio.VMSelector
$(top_srcdir)/include/java_io_ObjectInputStream.h: $(top_srcdir)/java/io/ObjectInputStream.java
	$(JAVAH) -o $@ java.io.ObjectInputStream
$(top_srcdir)/include/java_io_VMFile.h: $(top_srcdir)/vm/reference/java/io/VMFile.java
	$(JAVAH) -o $@ java.io.VMFile
$(top_srcdir)/include/java_io_VMObjectStreamClass.h: $(top_srcdir)/vm/reference/java/io/VMObjectStreamClass.java
	$(JAVAH) -o $@ java.io.VMObjectStreamClass
$(top_srcdir)/include/java_lang_Double.h: $(top_srcdir)/java/lang/Double.java
	$(JAVAH) -o $@ java.lang.Double
$(top_srcdir)/include/java_lang_Math.h: $(top_srcdir)/java/lang/Math.java
	$(JAVAH) -o $@ java.lang.Math
$(top_srcdir)/include/java_lang_Object.h: $(top_srcdir)/java/lang/Object.java
	$(JAVAH) -o $@ java.lang.Object
$(top_srcdir)/include/java_lang_VMDouble.h: $(top_srcdir)/vm/reference/java/lang/VMDouble.java
	$(JAVAH) -o $@ java.lang.VMDouble
$(top_srcdir)/include/java_lang_VMFloat.h: $(top_srcdir)/vm/reference/java/lang/VMFloat.java
	$(JAVAH) -o $@ java.lang.VMFloat
$(top_srcdir)/include/java_lang_VMProcess.h: $(top_srcdir)/vm/reference/java/lang/VMProcess.java
	$(JAVAH) -o $@ java.lang.VMProcess
$(top_srcdir)/include/java_lang_VMSystem.h: $(top_srcdir)/vm/reference/java/lang/VMSystem.java
	$(JAVAH) -o $@ java.lang.VMSystem
$(top_srcdir)/include/java_lang_reflect_Array.h: $(top_srcdir)/java/lang/reflect/Array.java
	$(JAVAH) -o $@ java.lang.reflect.Array
$(top_srcdir)/include/java_net_InetAddress.h: $(top_srcdir)/java/net/InetAddress.java
	$(JAVAH) -o $@ java.net.InetAddress
$(top_srcdir)/include/java_net_NetworkInterface.h: $(top_srcdir)/java/net/NetworkInterface.java
	$(JAVAH) -o $@ java.net.NetworkInterface
$(top_srcdir)/include/java_nio_VMDirectByteBuffer.h: $(top_srcdir)/vm/reference/java/nio/VMDirectByteBuffer.java
	$(JAVAH) -o $@ java.nio.VMDirectByteBuffer
$(top_srcdir)/include/java_nio_MappedByteBufferImpl.h: $(top_srcdir)/java/nio/MappedByteBufferImpl.java
	$(JAVAH) -o $@ java.nio.MappedByteBufferImpl
$(top_srcdir)/include/java_nio_channels_Channels.h: $(top_srcdir)/java/nio/channels/Channels.java
	$(JAVAH) -o $@ java.nio.channels.Channels
$(top_srcdir)/include/gnu_java_nio_channels_FileChannelImpl.h: $(top_srcdir)/gnu/java/nio/channels/FileChannelImpl.java
	$(JAVAH) -o $@ gnu.java.nio.channels.FileChannelImpl
$(top_srcdir)/include/java_util_VMTimeZone.h: $(top_srcdir)/vm/reference/java/util/VMTimeZone.java
	$(JAVAH) -o $@ java.util.VMTimeZone

endif # CREATE_JNI_HEADERS
