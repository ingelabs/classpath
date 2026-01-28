noinst_LTLIBRARIES = libclasspathnative.la
libclasspathnative_la_SOURCES = cpnet.c \
				cpnet.h \
				cpio.c \
				cpio.h \
				cpnative.h \
				cpproc.h \
				cpproc.c

# Helper binary for vfork-based process spawning
libexec_PROGRAMS = cpprochelper
cpprochelper_SOURCES = cpprochelper.c

AM_LDFLAGS = @CLASSPATH_CONVENIENCE@
AM_CPPFLAGS = @CLASSPATH_INCLUDES@ -DCPPROCHELPER_PATH='"$(libexecdir)/cpprochelper"'
AM_CFLAGS = @WARNING_CFLAGS@ @STRICT_WARNING_CFLAGS@ @ERROR_CFLAGS@ \
            @EXTRA_CFLAGS@
