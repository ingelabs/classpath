## Input file for automake to generate the Makefile.in used by configure

DIST_SUBDIRS = \
	Linux \
	SunOS \
	RTEMS \
	MinGW \
	embOS \
	posix \
	generic 

SUBDIRS = \
	$(TARGET) \
	generic

EXTRA_DIST = readme.txt

noinst_LTLIBRARIES = libtarget.la

libtarget_la_SOURCES =

libtarget_la_LIBADD = \
	generic/libtargetgeneric.la
