CC	 = gcc
LFLAGS   = -shared -Wl,-soname -Wl,libgtkpeer.so.0
objects  = native_state.o  gtklistpeer.o gtkwindowpeer.o \
	   gtkbuttonpeer.o gtkcomponentpeer.o  gtkfiledialogpeer.o \
	   gtkmainthread.o gtkscrollpanepeer.o gtkcheckboxpeer.o \
	   gtktoolkit.o	   gtkchoicepeer.o     gtktextcomponentpeer.o \
	   gtktextfieldpeer.o  gtktextareapeer.o gtklabelpeer.o gtkevents.o \
           gtkscrollbarpeer.o gdkgraphics.o gtkcanvaspeer.o gtkpanelpeer.o \
	   gtkcontainerpeer.o gtkdialogpeer.o
CPPFLAGS = -I/usr/local/jdk/include -I/usr/local/jdk/include/genunix
CFLAGS   = -Wall `gtk-config --cflags`
LDFLAGS  = `gtk-config --libs` `glib-config --libs gthread`
all: libgtkpeer.so.0.0.0 install

libgtkpeer.so.0.0.0: $(objects)
	$(CC) $(LFLAGS) $(LDFLAGS) -o libgtkpeer.so.0.0.0 $(objects) 

.PHONY : clean
clean : 
	-rm libgtkpeer.so.0.0.0 $(objects) $(objects:.o=.d) *~
	rm libgtkpeer.so.0 libgtkpeer.so Gtk*.h

# Generate auto dependencies
-include $(objects:.o=.d)
 
%.d: %.c
	$(SHELL) -ec '$(CC) -MM $(CFLAGS) $(CPPFLAGS) $< | sed '\''s/$*\\.o[ :]*/& $@/g'\'' > $@'

install: libgtkpeer.so.0.0.0
	ln -sf libgtkpeer.so.0.0.0 libgtkpeer.so.0
	ln -sf libgtkpeer.so.0.0.0 libgtkpeer.so
	cp libgtkpeer.so.0.0.0 /usr/local/lib
