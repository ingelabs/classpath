%.html : %.mhtml macros.mhtml
	mhc $< > $@

all : classpath.html status.html doc

.PHONY : clean
clean :
	-rm *.html

.PHONY : doc
doc :
	cd doc && $(MAKE)
