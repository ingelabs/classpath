## Input file for automake to generate the Makefile.in used by configure

SUBDIRS = lib native doc resource com gnu java vm javax

native: lib

EXTRA_DIST = HACKING BUGS THANKYOU mauve-classpath

dist-hook:
	-mkdir $(distdir)/include
	cp $(top_srcdir)/include/*.in $(distdir)/include
	cp $(top_srcdir)/include/*.h $(distdir)/include

ChangeLog:
	cvs2cl.pl -b --gmt --log-opts "-d'1998-06-30< now'" --window 7200 --usermap ChangeLog.usermap --ignore ChangeLog --fsf
