ifndef MHC
  MHC=mhc
endif

%.html : %.mhtml macros.mhtml
	$(MHC) $< > $@

all : classpath.html status.html index.html doc

index.html : classpath.html
	ln -s classpath.html index.html

.PHONY : clean
clean :
	-rm *.html

.PHONY : doc
doc :
	cd doc && $(MAKE)
