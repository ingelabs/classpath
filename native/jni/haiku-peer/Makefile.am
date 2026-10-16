# Haiku AWT and sound peer for GNU Classpath (libhaikupeer).
# Java side: gnu/java/awt/peer/haiku and gnu/javax/sound/sampled/haiku.

nativeexeclib_LTLIBRARIES = libhaikupeer.la

libhaikupeer_la_SOURCES = gnu_java_awt_peer_haiku_HaikuNative.cpp \
	gnu_java_awt_peer_haiku_HaikuRaster.cpp

libhaikupeer_la_LIBADD = -lbe -ltracker -lmedia -ltranslation

AM_LDFLAGS = @CLASSPATH_MODULE@
AM_CPPFLAGS = @CLASSPATH_INCLUDES@
AM_CXXFLAGS = -Wall -Wno-unused-parameter
