# We do not really support building with javac anymore but we can
# resurrect this...
# --cbj
## used by automake to generate Makefile.in

MAKEFILE_DEPEND = makefile.dep
HEADERS_DEPEND = headers.dep
CLASSES_DEPEND = classes.dep
JAVA_DEPEND = java.dep

## silently try to include these, if it fails gnu make
## will remake these 'makefiles' with the rules given in
## this file and restart the make process again
sinclude $(JAVA_DEPEND)
sinclude $(HEADERS_DEPEND)


#-----------------------------------------------------------------
# defining a javac program written in java
#-----------------------------------------------------------------
KJC_MAIN = at.dms.kjc.Main
SUN_JAVAC_MAIN = sun.tools.javac.Main

if USER_SPECIFIED_KJC
JAVAC_MAIN = $(KJC_MAIN)
else
JAVAC_MAIN = $(SUN_JAVAC_MAIN)
endif
#-----------------------------------------------------------------


#-----------------------------------------------------------------
# defining how to designate classpath for a vm and a javac type 
# program
#-----------------------------------------------------------------
if JAPHAR
if USER_SPECIFIED_JABBA
ARG_CLASSPATH_VM = -classpath
else
ARG_CLASSPATH_VM = --classpath
endif
else
ARG_CLASSPATH_VM = -classpath
endif

if USER_SPECIFIED_KJC
ARG_CLASSPATH_JAVAC = --classpath
else
ARG_CLASSPATH_JAVAC = -classpath
endif
#-----------------------------------------------------------------


#-----------------------------------------------------------------
# defining a java-like program
#-----------------------------------------------------------------
if USER_SPECIFIED_JABBA
USER_JABBA = @USER_JABBA@
else

if JAPHAR
USER_JABBA = @JAPHAR_JABBA@
else

if KAFFE
USER_JABBA = @KAFFE_JABBA@
else
USER_JABBA = java
endif

endif

endif
#-----------------------------------------------------------------


#-----------------------------------------------------------------
# defining the classpath for the java-like program, and 
# the javac-like program
#-----------------------------------------------------------------
if USER_SPECIFIED_CLASSLIB
USER_CLASSLIB = @USER_CLASSLIB@
else

if JAPHAR
USER_CLASSLIB = @JAPHAR_CLASSLIB@
else

if KAFFE
USER_CLASSLIB = @KAFFE_CLASSLIB@
else
USER_CLASSLIB = /usr/local/java/lib/classes.zip
endif

endif

endif

#-----------------------------------------------------------------


#-----------------------------------------------------------------
# handling javac definition including programs like jikes here
#-----------------------------------------------------------------
if USER_SPECIFIED_KJC
JAVAC = $(USER_JABBA) $(ARG_CLASSPATH_VM) @KJC_CLASSPATH@:$(USER_CLASSLIB) $(JAVAC_MAIN) $(ARG_CLASSPATH_JAVAC) .:$(USER_CLASSLIB) -d . @classes
else

if USER_SPECIFIED_JIKES

if USER_SPECIFIED_CLASSLIB
JAVAC = @JIKES@ -nowarn +CSO +F $(ARG_CLASSPATH_JAVAC) ..:../vm/current:.:@USER_CLASSLIB@ -d . @classes
else
JAVAC = @JIKES@ -nowarn +CSO +F $(ARG_CLASSPATH_JAVAC) ..:../vm/current:. -d . @classes
endif

else
JAVAC = $(USER_JABBA) $(ARG_CLASSPATH_VM) $(USER_CLASSLIB) $(JAVAC_MAIN) $(ARG_CLASSPATH_JAVAC) .:$(USER_CLASSLIB) -d . $(JAVA_SRCS)
endif

endif
#-----------------------------------------------------------------


#-----------------------------------------------------------------
# defining how to designate classpath for a vm and a javac type 
# program
#-----------------------------------------------------------------
if JAPHAR

if USER_SPECIFIED_JAVAH
ARG_JNI_JAVAH = -jni
else
ARG_JNI_JAVAH = --jni
endif

else
ARG_JNI_JAVAH = -jni
endif
#-----------------------------------------------------------------


#-----------------------------------------------------------------
# defining a javah-like program
#-----------------------------------------------------------------
if USER_SPECIFIED_JAVAH
USER_JAVAH = @USER_JAVAH@
else

if JAPHAR
USER_JAVAH = @JAPHAR_JAVAH@
else

if KAFFE
USER_JAVAH = @KAFFE_JAVAH@
else
USER_JAVAH = javah
endif

endif

endif
#-----------------------------------------------------------------


#-----------------------------------------------------------------
# defining classpath arg for javah-like program
#-----------------------------------------------------------------
if JAPHAR

if USER_SPECIFIED_JAVAH
ARG_CLASSPATH_JAVAH = -classpath
else
ARG_CLASSPATH_JAVAH = --classpath
endif

else
ARG_CLASSPATH_JAVAH = -classpath
endif
#-----------------------------------------------------------------

JAVAH = $(USER_JAVAH) $(ARG_JNI_JAVAH) $(ARG_CLASSPATH_JAVAH) .:$(USER_CLASSLIB)


#pkgdata_DATA = glibj.zip
install-data-local :
	unzip -d $(pkgdatadir) glibj.zip

all: $(HEADERS_DEPEND) glibj.zip $(MAKEFILE_DEPEND) $(HEADERS)
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
	$(JAVAC)
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
MAINTAINERCLEANFILES = makefile.dep $(JAVA_DEPEND) $(HEADERS_DEPEND)

maintainer-clean-local:
	-rm -rf gnu
	-rm -rf java
	-rm -rf javax
	-rm -rf kaffe
