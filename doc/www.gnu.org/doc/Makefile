ifndef MHC
  MHC=mhc
endif

%.html : %.mhtml
	$(MHC) $< > $@

%.mhtml : %.texinfo
	texi2html -monolithic $<; 
	sed -e 's/<BODY>/<BODY BGCOLOR="#FFFFFF" TEXT="#000000" LINK="#1F00FF" ALINK="#FF0000" VLINK="#9900DD">/' -e 's/<\/BODY>/<include footer.mhtml><footer><\/BODY>/' $(subst .mhtml,.html,$(@)) > $@;
	rm $(subst .mhtml,.html,$(@));

VPATH = ../..

all : hacking.html vmintegration.html

.PHONY : clean
clean :
	-rm *.html
