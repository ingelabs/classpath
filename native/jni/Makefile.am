## Input file for automake to generate the Makefile.in used by configure

if CREATE_GTK_PEER_LIBRARIES
  GTKDIR = gtk-peer
endif

SUBDIRS = classpath java-io java-lang java-net java-nio java-util $(GTKDIR)
DIST_SUBDIRS = classpath java-io java-lang java-net java-nio java-util gtk-peer

all-local:
	cd $(top_srcdir) && scripts/check_jni_methods.sh
