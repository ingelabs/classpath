TEXI_SRC	:= $(wildcard ../../*.texinfo)
MHTML_SRC	:= $(patsubst ../../%, %, $(TEXI_SRC:.texinfo=.mhtml)) $(wildcard *.mhtml)
HTML_OBJS	:= $(MHTML_SRC:.mhtml=.html)
VPATH = ../..

ifndef MHC
  export MHC=mhc
endif

%.mhtml : %.texinfo
	texi2html -monolithic $<;
	sed -e 's/<BODY>/<BODY BGCOLOR="#FFFFFF" TEXT="#000000" LINK="#1F00FF" ALINK="#FF0000" VLINK="#9900DD">/' -e 's/<\/BODY>/<include footer.mhtml><footer><\/BODY>/' $(subst .mhtml,.html,$(@)) > $@;
	-rm $(subst .mhtml,.html,$(@));

%.html : %.mhtml footer.mhtml
	$(MHC) $< > $@

all : $(HTML_OBJS)

.PHONY : clean
clean :
	-rm *.html

