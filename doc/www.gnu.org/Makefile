ifndef MHC
  MHC=mhc
endif

%.html : %.mhtml macros.mhtml
	$(MHC) $< > $@

all : classpath.html status.html doc

.PHONY : clean
clean :
	-rm *.html

.PHONY : doc
doc :
	cd doc && $(MAKE)
