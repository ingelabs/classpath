# We do not really support building with javac anymore but we can
# resurrect this...
# --cbj
## used by automake to generate Makefile.in
## for JAVAC
## if USER_SPECIFIED_JABBA
if USER_SPECIFIED_JABBA
##   if USER_SPECIFIED_CLASSLIB
if USER_SPECIFIED_CLASSLIB
##     JAVAC = 
JAVAC = @USER_JABBA@ -classpath @USER_CLASSLIB@ sun.tools.javac.Main -classpath ../lib:@USER_CLASSLIB@ -d ../lib
##   else
else
##     JAVAC = 
JAVAC = @USER_JABBA@ -classpath /usr/local/java/lib/classes.zip sun.tools.javac.Main -classpath ../lib:/usr/local/java/lib/classes.zip -d ../lib
##   endif
endif
## else
else 
##   if JAPHAR
if JAPHAR
##     JAVAC = 
JAVAC = @JAPHAR_JABBA@ --classpath @JAPHAR_CLASSLIB@ sun.tools.javac.Main -classpath ../lib:@JAPHAR_CLASSLIB@ -d ../lib  
##   else
else
##     if USER_SPECIFIED_CLASSLIB
if USER_SPECIFIED_CLASSLIB
##       JAVAC = 
JAVAC = java -classpath @USER_CLASSLIB@ sun.tools.javac.Main -classpath ../lib:@USER_CLASSLIB@ -d ../lib
##     else
else
##       JAVAC = 
JAVAC = java -classpath /usr/local/java/lib/classes.zip sun.tools.javac.Main -classpath ../lib:/usr/local/java/lib/classes.zip -d ../lib
##     endif
endif
##   endif
endif
## endif
endif

## for JIKES
if USER_SPECIFIED_JIKES
JAVAC = @JIKES@ -classpath $(top_srcdir):$(top_srcdir)/vm/reference:$(top_srcdir)/lib -d $(top_srcdir)/lib @@classes
endif

## for JAVAH
## if USER_SPECIFIED_JAVAH
if USER_SPECIFIED_JAVAH
##   if USER_SPECIFIED_CLASSLIB
if USER_SPECIFIED_CLASSLIB
##     JAVAH = 
JAVAHEADER = @USER_JAVAH@ -classpath ../lib:@USER_CLASSLIB@ -d ../include
##   else
else
##     JAVAH = 
JAVAHEADER = @USER_JAVAH@ -classpath ../lib:/usr/local/java/lib/classes.zip -d ../include
##   endif
endif
## else
else 
##   if JAPHAR
if JAPHAR
##     JAVAH = 
JAVAHEADER = @JAPHAR_JAVAH@ --classpath ../lib:@JAPHAR_CLASSLIB@ -d ../include  
##   else
else
##     if USER_SPECIFIED_CLASSLIB
if USER_SPECIFIED_CLASSLIB
##       JAVAH = 
JAVAHEADER = javah -classpath ../lib:@USER_CLASSLIB@ -d ../include
##     else
else
##       JAVAH = 
JAVAHEADER = javah -classpath ../lib:/usr/local/java/lib/classes.zip -d ../include
##     endif
endif
##   endif
endif
## endif
endif

MAKEFILE_DEPEND = makefile.dep
HEADERS_DEPEND = headers.dep
CLASSES_DEPEND = classes.dep

#data_DATA = glibj.zip
install-data-local :
	unzip -d $(datadir) glibj.zip

#all: $(CLASSES) glibj.zip

if DEVELOPER_BUILD
%.class: %.java
	$(JAVAC) $<

%.h: $(subst _, /, %.class)
	$(JAVAH) $<

default: all
all: glibj.zip

glibj.zip: $(MAKEFILE_DEPEND) $(CLASSES)
	-mkdir gnu
	-mkdir gnu/java
	-mkdir gnu/java/locale
	cp $(top_srcdir)/gnu/java/locale/*.properties gnu/java/locale/
	cp $(top_srcdir)/gnu/java/locale/*.uni gnu/java/locale/
	zip -r -D glibj.zip gnu java javax

$(MAKEFILE_DEPEND) : 
	touch makefile.depend
else

if MAINTAINER_BUILD

JUNK = $(SHELL) -c "cd $top_builddir/lib && ./mkdep.pl -h"
include $(HEADERS_DEPEND)
JUNK2 = $(SHELL) -c "cd $top_builddir/lib && ./mkdep.pl -c"
include $(CLASSES_DEPEND)

default : all
all: $(CLASSES) glibj.zip $(MAKEFILE_DEPEND)

glibj.zip: Makefile $(CLASSES)
	-mkdir gnu
	-mkdir gnu/java
	-mkdir gnu/java/locale
	cp $(top_srcdir)/gnu/java/locale/*.properties gnu/java/locale/
	cp $(top_srcdir)/gnu/java/locale/*.uni gnu/java/locale/
	# do something with all the .u files from jikes
	zip -r -D glibj.zip gnu java javax

$(MAKEFILE_DEPEND): Makefile classes $(CLASSES)
	./mkdep.pl -d
	include $(MAKEFILE_DEPEND)

classes: gen-classlist.sh standard.omit
	$(SHELL) -c "cd ${top_srcdir}/lib; ./gen-classlist.sh standard"

$(CLASSES):
	@JIKES@ -classpath ..:../vm/reference:. +CSO +M -d . @classes 2>errors.txt

else
## neither developer nor maintainer...

MDEPEND = $(SHELL test -r $(MAKEFILE_DEPEND) && echo "true")
@REMOVE@ifeq ($(MDEPEND),true)
@REMOVE@	include $(MAKEFILE_DEPEND)
@REMOVE@else
@REMOVE@	$(SHELL) -c "cd $(top_srcdir)/lib; ./mkdep.pl -d"
@REMOVE@	include $(MAKEFILE_DEPEND)
@REMOVE@endif

HDEPEND = $(SHELL test -r $(HEADERS_DEPEND) && echo "true")
@REMOVE@ifeq ($(HDEPEND),true)
@REMOVE@	include $(HEADERS_DEPEND)
@REMOVE@else
@REMOVE@	$(SHELL) -c "cd $(top_srcdir)/lib; ./mkdep.pl -h"
@REMOVE@	include $(HEADERS_DEPEND)
@REMOVE@endif

CDEPEND = $(SHELL test -r $(CLASSES_DEPEND) && echo "true")
@REMOVE@ifeq ($(CDEPEND),true)
@REMOVE@	include $(CLASSES_DEPEND)
@REMOVE@else
@REMOVE@	$(SHELL) -c "cd $(top_srcdir)/lib; ./mkdep.pl -c"
@REMOVE@	include $(CLASSES_DEPEND)
@REMOVE@endif

endif

endif

EXTRA_DIST = glibj.zip standard.omit
## not sure if we want to let the user easily get rid of glibj.zip
## CLEANFILES = glibj.zip
DISTCLEANFILES = $(CLASSES) $(HEADERS)
MAINTAINERCLEANFILES = makefile.dep

