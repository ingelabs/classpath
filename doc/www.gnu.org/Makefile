TOPLEVEL	:= .
SUBDIRS 	:= announce

DIRS		:= $(SUBDIRS) $(TOPLEVEL) 

ifndef MHC
  MHC=mhc
endif

VPATH = ..

TEXI_SRC	:= $(wildcard ../*.texinfo)
MHTML_OBJS	:= $(patsubst ../%, doc/%, $(TEXI_SRC:.texinfo=.mhtml))

MHTML_SRC	:= $(foreach dir, $(DIRS), $(wildcard $(dir)/*.mhtml))
HTML_OBJS	:= $(MHTML_SRC:.mhtml=.html) $(MHTML_OBJS:.mhtml=.html)

%.texinfo :
	cp ../$(notdir $(@)) doc

%.mhtml : %.texinfo
	$(SHELL) -c "cd $(dir $(@)) && texi2html -monolithic $(notdir $<)";
	sed -e 's/<BODY>/<BODY BGCOLOR="#FFFFFF" TEXT="#000000" LINK="#1F00FF" ALINK="#FF0000" VLINK="#9900DD">/' -e 's/<\/BODY>/<include footer.mhtml><footer><\/BODY>/' $(subst .mhtml,.html,$(@)) > $@;
	-rm $(subst .mhtml,.html,$(@));

%.html : %.mhtml $(TOPLEVEL)/macros.mhtml
	$(MHC) $< > $@

all : $(HTML_OBJS) index.html subdirs

index.html : classpath.html
	ln -s classpath.html index.html

.PHONY : clean
clean :
	$(foreach dir, $(SUBDIRS), cd $(dir) && $(MAKE) clean)
	cd doc && $(MAKE) clean
	-rm *.html

.PHONY : subdirs
subdirs :
	$(foreach dir, $(SUBDIRS), cd $(dir) && $(MAKE))

