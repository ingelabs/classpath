%.html : %.mhtml macros.mhtml
	mhc $< > $@

%.html : %.texinfo
	texi2html -monolithic $<; 
	mv $@ $@.tmp;
	sed -e 's/<BODY>/<BODY BGCOLOR="#FFFFFF" TEXT="#000000" LINK="#1F00FF" ALINK="#FF0000" VLINK="#9900DD">/' $@.tmp > $@;
	rm $@.tmp;

all : classpath.html status.html doc

.PHONY : clean
clean :
	-rm *.html

.PHONY : doc
doc :
	cd doc && $(MAKE)
