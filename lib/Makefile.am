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
JAVAC = @JIKES@ -classpath $(top_srcdir):$(top_srcdir)/vm/reference:$(top_srcdir)/lib:@USER_CLASSLIB@ -d $(top_srcdir)/lib @classes
endif

## for JAVAH
## if USER_SPECIFIED_JAVAH
if USER_SPECIFIED_JAVAH
##   if USER_SPECIFIED_CLASSLIB
if USER_SPECIFIED_CLASSLIB
##     JAVAH = 
JAVAH = @USER_JAVAH@ -jni -classpath ../lib:@USER_CLASSLIB@ 
##   else
else
##     JAVAH = 
JAVAH = @USER_JAVAH@ -jni -classpath ../lib
##   endif
endif
## else
else 
##   if JAPHAR
if JAPHAR
##     JAVAH = 
JAVAH = @JAPHAR_JAVAH@ --jni --classpath ../lib:@USER_CLASSLIB@
##   else
else
##     if USER_SPECIFIED_CLASSLIB
if USER_SPECIFIED_CLASSLIB
##       JAVAH = 
JAVAH = javah -jni -classpath ../lib:@USER_CLASSLIB@
##     else
else
##       JAVAH = 
JAVAH = javah -jni -classpath ../lib:/usr/local/java/lib/classes.zip
##     endif
endif
##   endif
endif
## endif
endif

MAKEFILE_DEPEND = makefile.dep
HEADERS_DEPEND = headers.dep
CLASSES_DEPEND = classes.dep
JAVA_DEPEND = java.dep

#data_DATA = glibj.zip
install-data-local :
	unzip -d $(datadir) glibj.zip

include $(JAVA_DEPEND)
include $(HEADERS_DEPEND)

all: glibj.zip $(MAKEFILE_DEPEND) $(HEADERS_DEPEND) $(HEADERS)
#jar-classes

# this makes headers always generate after making glibj.zip
# $(HEADERS) : glibj.zip 

#.PHONY: jar-classes
glibj.zip: classes compile-classes
	if ! [ -e gnu ]; then -mkdir gnu; fi
	if ! [ -e gnu/java ]; then -mkdir gnu/java; fi
	if ! [ -e gnu/java/locale ]; then -mkdir gnu/java/locale; fi
	cp $(top_srcdir)/gnu/java/locale/*.properties gnu/java/locale/
	cp $(top_srcdir)/gnu/java/locale/*.uni gnu/java/locale/
	# do something with all the .u files from jikes
	$(ZIP) -r -D glibj.zip gnu java javax > /dev/null

classes: gen-classlist.sh standard.omit $(top_srcdir)/java $(top_srcdir)/javax $(top_srcdir)/gnu
	$(SHELL) -c "cd $(top_srcdir)/lib && ./gen-classlist.sh standard"

.PHONY: dep
dep: classes
	$(SHELL) -c "cd $(top_srcdir)/lib; ./mkdep.pl -s"

#.PHONY: compile-classes
compile-classes: classes $(JAVA_DEPEND) $(JAVA_SRCS) Makefile
if USER_SPECIFIED_CLASSLIB
	$(SHELL) -c "@JIKES@ -nowarn -classpath ..:../vm/reference:.:@USER_CLASSLIB@ +CSO +M +F -d . @classes 2>errors.txt; exit 0"
else
	$(SHELL) -c "@JIKES@ -nowarn -classpath ..:../vm/reference:. +CSO +M +F -d . @classes 2>errors.txt; exit 0"
endif
	touch compile-classes

$(MAKEFILE_DEPEND): classes $(HEADERS_DEPEND) $(CLASSES_DEPEND)
	$(SHELL) -c "cd $(top_srcdir)/lib && ./mkdep.pl -d"

$(HEADERS_DEPEND): classes
	if ! [ -e $(top_srcdir)/include ]; then -mkdir $(top_srcdir)/include; fi	
	$(SHELL) -c "cd $(top_srcdir)/lib && ./mkdep.pl -h"

$(CLASSES_DEPEND): classes
	$(SHELL) -c "cd $(top_srcdir)/lib && ./mkdep.pl -c"

$(JAVA_DEPEND): classes
	$(SHELL) -c "cd $(top_srcdir)/lib && ./mkdep.pl -j"

## neither developer nor maintainer...

##MDEPEND = $(SHELL test -r $(MAKEFILE_DEPEND) && echo "true")
##@REMOVE@ifeq ($(MDEPEND),true)
##@REMOVE@	include $(MAKEFILE_DEPEND)
##@REMOVE@else
##@REMOVE@	$(SHELL) -c "cd $(top_srcdir)/lib; ./mkdep.pl -d"
##@REMOVE@	include $(MAKEFILE_DEPEND)
##@REMOVE@endif

##HDEPEND = $(SHELL test -r $(HEADERS_DEPEND) && echo "true")
##@REMOVE@ifeq ($(HDEPEND),true)
##@REMOVE@	include $(HEADERS_DEPEND)
##@REMOVE@else
##@REMOVE@	$(SHELL) -c "cd $(top_srcdir)/lib; ./mkdep.pl -h"
##@REMOVE@	include $(HEADERS_DEPEND)
##@REMOVE@endif

##CDEPEND = $(SHELL test -r $(CLASSES_DEPEND) && echo "true")
##@REMOVE@ifeq ($(CDEPEND),true)
##@REMOVE@	include $(CLASSES_DEPEND)
##@REMOVE@else
##@REMOVE@	$(SHELL) -c "cd $(top_srcdir)/lib; ./mkdep.pl -c"
##@REMOVE@	include $(CLASSES_DEPEND)
##@REMOVE@endif

##endif

##endif

EXTRA_DIST = glibj.zip standard.omit
## not sure if we want to let the user easily get rid of glibj.zip
## CLEANFILES = glibj.zip
DISTCLEANFILES = $(CLASSES) $(HEADERS)
MAINTAINERCLEANFILES = makefile.dep

