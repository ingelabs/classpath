ifndef MHC
  MHC=mhc
endif

%.html : %.mhtml ../macros.mhtml
	$(MHC) $< > $@

all : 19990206.html

index.html : 19990206.html
	ln -s 19990206.html index.html

.PHONY : clean
clean :
	-rm *.html
