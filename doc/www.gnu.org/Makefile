SUBDIRS 	:= announce doc

ifndef MHC
  export MHC=mhc
endif

MHTML_SRC	:= $(wildcard *.mhtml)
HTML_OBJS	:= $(MHTML_SRC:.mhtml=.html) 

%.texinfo :
	cp ../$(notdir $(@)) doc

%.html : %.mhtml macros.mhtml
	$(MHC) $< > $@

all : $(HTML_OBJS) index.html subdirs

index.html : classpath.html
	ln -s classpath.html index.html

.PHONY : clean
clean :
	$(foreach dir, $(SUBDIRS), sh -c "cd $(dir) && $(MAKE) clean";)
	-rm *.html

.PHONY : subdirs
subdirs :
	$(foreach dir, $(SUBDIRS), sh -c "cd $(dir) && $(MAKE)";)

