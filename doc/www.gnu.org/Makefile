SUBDIRS 	:= announce doc

ifndef WMK
  export WMK=wmk
endif

WML_SRC = $(wildcard *.wml)
WML_EN_OBJS = $(patsubst %.wml,%.en.html,$(WML_SRC))

%.texinfo :
	cp ../$(notdir $(@)) doc

%.en.html: %.wml include/macros.wml
	$(WMK) -f $<
	-rm -f $(patsubst %.wml,%.html,$<)

all : $(WML_EN_OBJS) subdirs

.PHONY : clean
clean :
	$(foreach dir, $(SUBDIRS), sh -c "cd $(dir) && $(MAKE) clean";)
	-rm -f *.html

.PHONY : subdirs
subdirs :
	$(foreach dir, $(SUBDIRS), sh -c "cd $(dir) && $(MAKE)";)


