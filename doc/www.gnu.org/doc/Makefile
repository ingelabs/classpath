# If you find this sed script failing, then you probably don't have
# sed 3.02.80+ currently available on ftp://alpha.gnu.org/pub/sed/

SUBDIRS=

ifndef WMK
  export WMK=wmk
endif

TEXI_SRC	:= $(wildcard ../../*.texinfo)
VPATH = ../..
WML_SRC = $(patsubst ../../%, %, $(TEXI_SRC:.texinfo=.wml)) $(wildcard *.wml)
WML_EN_OBJS = $(patsubst %.wml,%.en.html,$(WML_SRC))

%.wml : %.texinfo
	texi2html -monolithic $<;
	sed -e 's/<HTML>/\#!wml --include=..\n\#use wml::std::lang\n\#use wml::fmt::isolatin\n\#use wml::std::case global=upper\n<lang:new id=en short>\n<lang:star:slice:>\n\#include <include\/macros.wml>\n<HTML>/' -e 's/<BODY>/<BODY BGCOLOR="#FFFFFF" TEXT="#000000" LINK="#1F00FF" ALINK="#FF0000" VLINK="#9900DD">/' -e 's/<\/BODY>/<footer>\n<\/BODY>/' $(subst .wml,.html,$(@)) > $@;
	-rm -f $(subst .wml,.html,$(@))

%.en.html: %.wml ../include/macros.wml
	$(WMK) -f $<
	-rm -f $(patsubst %.wml,%.html,$<)

.PHONY : all
all : $(WML_EN_OBJS) subdirs

.PHONY : clean
clean :
	$(foreach dir, $(SUBDIRS), sh -c "cd $(dir) && $(MAKE) clean";)
	-rm -f *.html
	-rm -f hacking.wml vmintegration.wml

.PHONY : subdirs
subdirs :
	$(foreach dir, $(SUBDIRS), sh -c "cd $(dir) && $(MAKE)";)

.PHONY : publish
publish : 
	@sh -c "if [ "$(CLASSPATH_WEBROOT)x" = x ]; then echo 'You must set CLASSPATH_WEBROOT in your environment first'; exit 1; fi"
	@sh -c "if [ ! -d $(CLASSPATH_WEBROOT) ]; then echo 'You must set CLASSPATH_WEBROOT in your environment first'; exit 1; fi"
	$(foreach dir, $(SUBDIRS), sh -c "cd $(dir) && $(MAKE) publish";)
	-cp -f *.html $(CLASSPATH_WEBROOT)/doc/

