## Input file for automake to generate the Makefile.in used by configure

SUBDIRS = lib native doc resource com gnu java vm javax

native: lib

EXTRA_DIST = HACKING BUGS THANKYOU

dist-hook:
	cp -R $(top_srcdir)/include $(top_distdir)

