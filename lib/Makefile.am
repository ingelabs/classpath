# We do not really support building with javac anymore but we can
# resurrect this...
# --cbj
## used by automake to generate Makefile.in

##PERL = @PERL@
JAVA_DEPEND = java.dep

## silently try to include these, if it fails gnu make
## will remake these 'makefiles' with the rules given in
## this file and restart the make process again
sinclude $(JAVA_DEPEND)

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


if HAVE_ZIP
pkgdata_DATA = glibj.zip
noinst_DATA = genclasses classes
else
noinst_DATA = genclasses classes glibj.zip
endif

install-data-local :
	-$(top_srcdir)/mkinstalldirs $(pkgdatadir)
	if ! [ -e glibj.zip ]; then cp -R com $(pkgdatadir); fi
	if ! [ -e glibj.zip ]; then cp -R gnu $(pkgdatadir); fi
	if ! [ -e glibj.zip ]; then cp -R java $(pkgdatadir); fi
	if ! [ -e glibj.zip ]; then cp -R javax $(pkgdatadir); fi

glibj.zip: classes compile-classes
	if ! [ -e gnu ]; then mkdir gnu; fi
	if ! [ -e gnu/java ]; then mkdir gnu/java; fi
	if ! [ -e gnu/java/locale ]; then mkdir gnu/java/locale; fi
	cp $(top_srcdir)/gnu/java/locale/*.properties gnu/java/locale/
	cp $(top_srcdir)/gnu/java/locale/*.uni gnu/java/locale/
	if test "$(ZIP)" != ""; then $(ZIP) -r -D glibj.zip com gnu java javax > /dev/null; fi

.PHONY: genclasses

classes: gen-classlist.sh standard.omit
	top_builddir=$(top_builddir) $(SHELL) ./gen-classlist.sh standard

genclasses: gen-classlist.sh standard.omit
	top_builddir=$(top_builddir) $(SHELL) ./gen-classlist.sh standard

$(JAVA_DEPEND): genclasses

compile-classes: classes $(JAVA_SRCS) Makefile
	$(JAVAC)
	touch compile-classes

EXTRA_DIST = standard.omit mkcollections.pl.in
## not sure if we want to let the user easily get rid of glibj.zip
## CLEANFILES = glibj.zip
DISTCLEANFILES = 
MAINTAINERCLEANFILES = classes $(JAVA_DEPEND)

maintainer-clean-local:
	-rm -rf com
	-rm -rf gnu
	-rm -rf java
	-rm -rf javax
	-rm -rf kaffe
