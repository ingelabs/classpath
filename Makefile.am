## Input file for automake to generate the Makefile.in used by configure

# lib first, to compile .class files before native code, last examples
SUBDIRS = lib doc include native resource examples

native: lib

EXTRA_DIST = HACKING BUGS THANKYOU mauve-classpath LICENSE

#DISTCHECK_CONFIGURE_FLAGS = --enable-gjdoc

dist-hook:
	-$(mkinstalldirs) $(distdir)/include
	-cp -f $(top_srcdir)/include/*.in $(distdir)/include
	-cp -f $(top_srcdir)/include/*.h $(distdir)/include

ChangeLog:
	cvs2cl.pl -b --gmt --log-opts "-d'1998-06-30< now'" --window 7200 --usermap ChangeLog.usermap --ignore ChangeLog --fsf
