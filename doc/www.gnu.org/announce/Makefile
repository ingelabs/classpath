MHTML_SRC	:= $(wildcard *.mhtml)
HTML_OBJS	:= $(MHTML_SRC:.mhtml=.html)

%.html : %.mhtml ../macros.mhtml
	$(MHC) $< > $@

all : $(HTML_OBJS) index.html

index.html : 19990206.html
	ln -s 19990206.html index.html

.PHONY : clean
clean :
	-rm *.html
