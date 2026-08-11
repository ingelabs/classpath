noinst_LTLIBRARIES = libclasspathnative.la
libclasspathnative_la_SOURCES = cpnet.c \
				cpnet.h \
				cpio.c \
				cpio.h \
				cpnative.h \
				cpproc.h \
				cpproc.c \
				cpproc-child.h \
				cpproc-child.c

spawnhelperdir = $(pkglibdir)
spawnhelper_PROGRAMS = cpspawnhelper
cpspawnhelper_SOURCES = cpspawnhelper.c \
			cpproc-child.h \
			cpproc-child.c

# Keep the helper's objects separate from the libtool objects built from
# the same sources. Per-target CFLAGS trigger Automake's target-specific
# object names; assigning AM_CFLAGS leaves the effective flags unchanged.
cpspawnhelper_CFLAGS = $(AM_CFLAGS)

AM_LDFLAGS = @CLASSPATH_CONVENIENCE@
AM_CPPFLAGS = @CLASSPATH_INCLUDES@ \
	      -DCPPROC_SPAWN_HELPER='"$(pkglibdir)/cpspawnhelper"'
AM_CFLAGS = @WARNING_CFLAGS@ @STRICT_WARNING_CFLAGS@ @ERROR_CFLAGS@ \
            @EXTRA_CFLAGS@
