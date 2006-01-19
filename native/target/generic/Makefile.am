## Input file for automake to generate the Makefile.in used by configure

AM_CPPFLAGS = -I$(top_srcdir)/native/target/$(TARGET)

EXTRA_DIST = \
	target_generic_math.h \
	target_generic_memory.h \
	target_generic_file.h

noinst_LTLIBRARIES = libtargetgeneric.la

libtargetgeneric_la_SOURCES = \
	target_generic.c \
	target_generic.h \
	target_generic_io.c \
	target_generic_io.h \
	target_generic_misc.c \
	target_generic_misc.h \
	target_generic_network.c \
	target_generic_network.h
