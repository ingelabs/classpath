## Input file for automake to generate the Makefile.in used by configure

# lib first, to compile .class files before native code
SUBDIRS = lib doc external gnu java javax include native resource vm

native: lib

EXTRA_DIST = HACKING BUGS THANKYOU mauve-classpath LICENSE

#DISTCHECK_CONFIGURE_FLAGS = --enable-gjdoc

dist-hook:
	-$(mkinstalldirs) $(distdir)/include
	-cp -f $(top_srcdir)/include/*.in $(distdir)/include
	-cp -f $(top_srcdir)/include/*.h $(distdir)/include

ChangeLog:
	cvs2cl.pl -b --gmt --log-opts "-d'1998-06-30< now'" --window 7200 --usermap ChangeLog.usermap --ignore ChangeLog --fsf

indent:
	find .  | \
	grep \\.[ch]$$ | \
	awk ' \
	{print "indent " $$1 " -o " $$1 ".indented && \
	if cmp -s " $$1 " " $$1 ".indented; \
	then rm " $$1 ".indented; \
	else mv " $$1 " " $$1 ".bak; \
	mv " $$1 ".indented " $$1 "; \
	echo " $$1 "; \
	fi"} \
	' | \
	sh

