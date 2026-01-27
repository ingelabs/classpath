noinst_LTLIBRARIES = libclasspathnative.la
libclasspathnative_la_SOURCES = cpnet.c \
				cpnet.h \
				cpio.c \
				cpio.h \
				cpnative.h \
				cpproc.h \
				cpproc.c

pkglibexec_PROGRAMS = classpath-spawnhelper
classpath_spawnhelper_SOURCES = classpath-spawnhelper.c

AM_LDFLAGS = @CLASSPATH_CONVENIENCE@
AM_CPPFLAGS = @CLASSPATH_INCLUDES@ -DCP_SPAWNHELPER_PATH=\"$(pkglibexecdir)/classpath-spawnhelper\"
AM_CFLAGS = @WARNING_CFLAGS@ @STRICT_WARNING_CFLAGS@ @ERROR_CFLAGS@ \
            @EXTRA_CFLAGS@
