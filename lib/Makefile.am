# We do not really support building with javac anymore but we can
# resurrect this...
# --cbj
## used by automake to generate Makefile.in

PERL = @PERL@
MAKEFILE_DEPEND = makefile.dep
HEADERS_DEPEND = headers.dep
CLASSES_DEPEND = classes.dep
JAVA_DEPEND = java.dep

## silently try to include these, if it fails gnu make
## will remake these 'makefiles' with the rules given in
## this file and restart the make process again
sinclude $(JAVA_DEPEND)
sinclude $(HEADERS_DEPEND)

KJC_CLASSPATH=@KJC_CLASSPATH@

USER_CLASSLIB = @USER_CLASSLIB@

GCJ = @GCJ@
JIKES = @JIKES@
USER_JABBA = @USER_JABBA@
USER_JAVAH = @USER_JAVAH@

#-----------------------------------------------------------------
# defining a javac program written in java
#-----------------------------------------------------------------
KJC_MAIN = at.dms.kjc.Main
#-----------------------------------------------------------------


#-----------------------------------------------------------------
# defining how to designate classpath for a vm and a javac type 
# program
#-----------------------------------------------------------------
ARG_CLASSPATH_VM = -classpath

if USER_SPECIFIED_KJC
ARG_CLASSPATH_JAVAC = --classpath
else
ARG_CLASSPATH_JAVAC = -classpath
endif
#-----------------------------------------------------------------


#-----------------------------------------------------------------
# handling javac definition including programs like jikes here
#-----------------------------------------------------------------
if FOUND_JIKES
JAVAC = $(JIKES) -nowarn +F $(ARG_CLASSPATH_JAVAC) $(top_srcdir):$(top_builddir)/vm/current:.:$(USER_CLASSLIB) -d . @classes
else
if FOUND_GCJ
JAVAC = $(GCJ) -C -d . @classes
else
if FOUND_KJC
JAVAC = $(USER_JABBA) $(ARG_CLASSPATH_VM) $(KJC_CLASSPATH):$(USER_CLASSLIB) $(KJC_MAIN) $(ARG_CLASSPATH_JAVAC) .:$(USER_CLASSLIB) -d . @classes
endif 
endif
endif

#-----------------------------------------------------------------


#-----------------------------------------------------------------
# defining how to designate classpath for a vm and a javac type 
# program
#-----------------------------------------------------------------
ARG_JNI_JAVAH = -jni
#-----------------------------------------------------------------


#-----------------------------------------------------------------
# defining classpath arg for javah-like program
#-----------------------------------------------------------------
ARG_CLASSPATH_JAVAH = -classpath
#-----------------------------------------------------------------

JAVAH = $(USER_JAVAH) $(ARG_JNI_JAVAH) $(ARG_CLASSPATH_JAVAH) .:$(USER_CLASSLIB)


pkgdata_DATA = glibj.zip
install-data-local :
	if ! [ -e $(pkgdatadir) ]; then mkdir -p $(pkgdatadir); fi
	unzip -d $(pkgdatadir) glibj.zip

if CREATE_JNI_HEADERS
all: $(HEADERS_DEPEND) glibj.zip $(CP_HEADERS)
else
all: glibj.zip
endif

#jar-classes

# this makes headers always generate after making glibj.zip
$(CP_HEADERS) : headers.dep

#.PHONY: jar-classes
glibj.zip: classes compile-classes $(MAKEFILE_DEPEND)
	if ! [ -e gnu ]; then mkdir gnu; fi
	if ! [ -e gnu/java ]; then mkdir gnu/java; fi
	if ! [ -e gnu/java/locale ]; then mkdir gnu/java/locale; fi
	cp $(top_srcdir)/gnu/java/locale/*.properties gnu/java/locale/
	cp $(top_srcdir)/gnu/java/locale/*.uni gnu/java/locale/
	$(ZIP) -r -D glibj.zip gnu java javax > /dev/null

classes: gen-classlist.sh standard.omit
	top_builddir=$(top_builddir) $(SHELL) ./gen-classlist.sh standard

.PHONY: dep
dep: classes
	top_srcdir=@top_srcdir@ $(PERL) ./mkdep.pl -s

#.PHONY: compile-classes
compile-classes: classes $(JAVA_DEPEND) $(JAVA_SRCS) Makefile
	$(JAVAC)
	touch compile-classes

$(MAKEFILE_DEPEND): compile-classes
	top_srcdir=@top_srcdir@ $(PERL) ./mkdep.pl -d
#	$(SHELL) -c find -regex ".*\.u$$" -exec rm \{\} \; > /dev/null

$(HEADERS_DEPEND): classes
	if ! [ -e $(top_builddir)/include ]; then -mkdir $(top_builddir)/include; fi	
	top_srcdir=@top_srcdir@ $(PERL) ./mkdep.pl -h

$(CLASSES_DEPEND): classes
	top_srcdir=@top_srcdir@ $(PERL) ./mkdep.pl -c

$(JAVA_DEPEND): classes
	top_srcdir=@top_srcdir@ $(PERL) ./mkdep.pl -j

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

EXTRA_DIST = glibj.zip standard.omit mkcollections.pl.in
## not sure if we want to let the user easily get rid of glibj.zip
## CLEANFILES = glibj.zip
DISTCLEANFILES = $(CLASSES) $(CP_HEADERS)
MAINTAINERCLEANFILES = classes makefile.dep $(JAVA_DEPEND) $(HEADERS_DEPEND)

maintainer-clean-local:
	-rm -rf com
	-rm -rf gnu
	-rm -rf java
	-rm -rf javax
	-rm -rf kaffe
