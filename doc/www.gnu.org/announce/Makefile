MHTML_SRC	:= $(wildcard *.mhtml)
HTML_OBJS	:= $(MHTML_SRC:.mhtml=.html)

ifndef MHC
  export MHC=mhc
endif

%.html : %.mhtml ../macros.mhtml
	$(MHC) $< > $@

all : $(HTML_OBJS) index.html

index.html : announcements.html
	-rm index.html
	ln -s announcements.html index.html

.PHONY : clean
clean :
	-rm *.html
