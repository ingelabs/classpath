CC	 = javac
PACKAGE  = gnu.java.awt.peer.gtk

# This is where the JNI header files get copied to.
# This is in here because it is convenient to me.  We can do this another way.
NATIVEDIR = ../../../../../native/$(PACKAGE)

package  := $(subst .,_, $(PACKAGE))

objects  = GtkButtonPeer.class    GtkFileDialogPeer.class GtkPanelPeer.class \
	   GtkComponentPeer.class GtkFramePeer.class      GtkToolkit.class \
	   GtkContainerPeer.class GtkWindowPeer.class     GtkDialogPeer.class \
	   GtkMainThread.class    GtkScrollPanePeer.class GtkChoicePeer.class \
	   GtkGenericPeer.class   GtkCheckboxPeer.class   Test.class \
	   GtkTextComponentPeer.class     GtkTextFieldPeer.class \
	   GtkTextAreaPeer.class  GtkLabelPeer.class      GtkListPeer.class \
           GtkScrollbarPeer.class GdkGraphics.class GtkCanvasPeer.class
jniclass = GtkButtonPeer     GtkFileDialogPeer GtkComponentPeer \
	   GtkContainerPeer  GtkWindowPeer     GtkMainThread \
	   GtkScrollPanePeer GtkCheckboxPeer   GtkToolkit \
	   GtkChoicePeer     GtkTextComponentPeer \
	   GtkTextFieldPeer  GtkTextAreaPeer   GtkLabelPeer \
	   GtkListPeer       GtkScrollbarPeer GdkGraphics GtkCanvasPeer \
	   GtkPanelPeer      GtkDialogPeer
jnideps := $(foreach class,$(jniclass),$(package)_$(strip $(class)).h)

all: Test headers

Test: $(objects)

$(objects): %.class: %.java
	$(CC) $*.java

.PHONY: clean
clean:
	rm *.class *~ *.h

headers: $(jnideps)

#$(jnideps): %.h
#	echo $*

$(jnideps): $(package)_%.h: %.class
	javah -jni $(PACKAGE).$*
	cp $(package)_$*.h $(NATIVEDIR)/$*.h

run: Test
	java Test


