## Input file for automake to generate the Makefile.in used by configure

SUBDIRS = lib native doc resource gnu java vm

native: lib

EXTRA_DIST = BUGS THANKYOU

dist-hook:
	cp -R $(top_srcdir)/include $(top_distdir)

