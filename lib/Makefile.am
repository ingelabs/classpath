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


propertydirs :=  $(shell cd $(top_srcdir)/resource && $(FIND) gnu java -type d ! -regex .*CVS)
propertyfiles :=  $(shell cd $(top_srcdir)/resource && $(FIND) gnu java -name \*\.properties)

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
JAVAC = $(JIKES) +F -bootclasspath '' -extdirs '' -sourcepath '' $(ARG_CLASSPATH_JAVAC) $(top_srcdir):$(top_srcdir)/external/jaxp/source:$(top_builddir)/vm/current:.:$(USER_CLASSLIB) -d . @classes
else
if FOUND_GCJ
JAVAC = $(GCJ) --bootclasspath '' $(ARG_CLASSPATH_JAVAC) $(top_srcdir):$(top_srcdir)/external/jaxp/source:$(top_builddir)/vm/current:.:$(USER_CLASSLIB) -C -d . @classes
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

if BUILD_GLIBJ_ZIP
if HAVE_ZIP
dist_pkgdata_DATA = glibj.zip
noinst_DATA = genclasses
else
noinst_DATA = genclasses compile-classes resources
endif

install-data-local :
	-$(top_srcdir)/mkinstalldirs $(DESTDIR)$(pkgdatadir)
	if ! [ -e glibj.zip ]; then cp -R gnu $(DESTDIR)$(pkgdatadir); fi
	if ! [ -e glibj.zip ]; then cp -R java $(DESTDIR)$(pkgdatadir); fi
	if ! [ -e glibj.zip ]; then cp -R javax $(DESTDIR)$(pkgdatadir); fi
	if ! [ -e glibj.zip ]; then cp -R org $(DESTDIR)$(pkgdatadir); fi

# FIXME - should mimic doc/api/Makefile.am instead...
uninstall-local:
	rm -rf $(DESTDIR)$(pkgdatadir)/gnu
	rm -rf $(DESTDIR)$(pkgdatadir)/java
	rm -rf $(DESTDIR)$(pkgdatadir)/javax
	rm -rf $(DESTDIR)$(pkgdatadir)/org

endif # BUILD_GLIBJ_ZIP

.PHONY: genclasses

glibj.zip: classes compile-classes resources
	if test "$(ZIP)" != ""; then $(ZIP) -r -D glibj.zip gnu java javax org > /dev/null; fi

resources:
	if ! [ -e gnu ]; then mkdir gnu; fi
	if ! [ -e gnu/java ]; then mkdir gnu/java; fi
	if ! [ -e gnu/java/locale ]; then mkdir gnu/java/locale; fi
	@list='$(propertydirs)'; for p in $$list; do \
	  if ! [ -e $$p ]; then mkdir $$p; fi; \
	done
	@list='$(propertyfiles)'; for p in $$list; do \
	  cp $(top_srcdir)/resource/$$p $$p; \
	done
	touch resources

classes: genclasses

genclasses: gen-classlist.sh standard.omit
	top_builddir=$(top_builddir) $(SHELL) ./gen-classlist.sh standard

$(JAVA_DEPEND): genclasses

compile-classes: classes $(JAVA_SRCS) Makefile
	$(JAVAC)
	touch compile-classes

EXTRA_DIST = standard.omit mkcollections.pl.in
CLEANFILES = compile-classes resources classes glibj.zip classes.1 \
	$(JAVA_DEPEND)

clean-local:
	-rm -rf gnu
	-rm -rf java
	-rm -rf javax
	-rm -rf org
