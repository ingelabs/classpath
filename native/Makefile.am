## Input file for automake to generate the Makefile.in used by configure

if CREATE_JNI_LIBRARIES
  JNIDIR = jni
endif

if CREATE_GTK_PEER_LIBRARIES
  JAWTDIR = jawt
endif

DIST_SUBDIRS = \
	fdlibm \
	jni \
	jawt \
	target

# ATTENTION: target must be built before jni
SUBDIRS = \
	fdlibm \
	target \
	$(JNIDIR) \
	$(JAWTDIR)
