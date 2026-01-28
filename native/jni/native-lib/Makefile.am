noinst_LTLIBRARIES = libclasspathnative.la
libclasspathnative_la_SOURCES = cpnet.c \
				cpnet.h \
				cpio.c \
				cpio.h \
				cpnative.h \
				cpproc.h \
				cpproc.c

# Helper binary for vfork-based process spawning
cphelperdir = $(pkglibdir)
cphelper_PROGRAMS = binrunner
binrunner_SOURCES = binrunner.c

AM_LDFLAGS = @CLASSPATH_CONVENIENCE@
AM_CPPFLAGS = @CLASSPATH_INCLUDES@ -DBINRUNNER_PATH='"$(pkglibdir)/binrunner"'
AM_CFLAGS = @WARNING_CFLAGS@ @STRICT_WARNING_CFLAGS@ @ERROR_CFLAGS@ \
            @EXTRA_CFLAGS@
