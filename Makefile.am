## Input file for automake to generate the Makefile.in used by configure

# lib first, to compile .class files before native code
SUBDIRS = lib doc gnu java javax native resource vm

native: lib

EXTRA_DIST = HACKING BUGS THANKYOU mauve-classpath LICENSE

DISTCHECK_CONFIGURE_FLAGS = --enable-gjdoc

dist-hook:
	-$(mkinstalldirs) $(distdir)/include
	-cp -f $(top_srcdir)/include/*.in $(distdir)/include
	-cp -f $(top_srcdir)/include/*.h $(distdir)/include
	-$(mkinstalldirs) $(distdir)/external/jaxp
	-cp -f $(top_srcdir)/external/README $(distdir)/external
	-cp -Rf $(top_srcdir)/external/jaxp $(distdir)/external/jaxp

ChangeLog:
	cvs2cl.pl -b --gmt --log-opts "-d'1998-06-30< now'" --window 7200 --usermap ChangeLog.usermap --ignore ChangeLog --fsf
