JAVA_DEPEND = java.dep

## silently try to include these, if it fails gnu make
## will remake these 'makefiles' with the rules given in
## this file and restart the make process again
sinclude $(JAVA_DEPEND)

USER_CLASSLIB = @USER_CLASSLIB@

GCJ = @GCJ@
JIKES = @JIKES@
USER_JABBA = @USER_JABBA@
USER_JAVAH = @USER_JAVAH@
FIND = @FIND@

propertydirs :=  $(shell cd $(top_srcdir)/resource && $(FIND) gnu java org -type d ! -name CVS -print)
propertyfiles :=  $(shell cd $(top_srcdir)/resource && $(FIND) gnu java org -name \*\.properties -print)

# handling source to bytecode compiler programs like gcj, jikes  and kjc
if FOUND_GCJ
JAVAC = $(GCJ) --bootclasspath '' --classpath $(top_srcdir):$(top_builddir)/vm/current:.:$(USER_CLASSLIB) -C -d . @classes
else
if FOUND_JIKES
JAVAC = $(JIKES) +F -bootclasspath '' -extdirs '' -sourcepath '' --classpath $(top_srcdir):$(top_builddir)/vm/current:.:$(USER_CLASSLIB) -d . @classes
else
if FOUND_KJC
JAVAC = $(KJC) -classpath .:$(USER_CLASSLIB) -d . @classes
endif # FOUND_KJC
endif # FOUND_GCJ
endif # FOUND_JIKES

JAVAH = $(USER_JAVAH) -jni -classpath .:$(USER_CLASSLIB)

if INSTALL_GLIBJ_ZIP

pkgdata_DATA = glibj.zip

endif # INSTALL_GLIBJ_ZIP

if INSTALL_CLASS_FILES

noinst_DATA = genclasses compile-classes resources

install-data-local: genclasses compile-classes
	-$(top_srcdir)/mkinstalldirs $(DESTDIR)$(pkgdatadir)
	cp -R gnu $(DESTDIR)$(pkgdatadir)
	cp -R java $(DESTDIR)$(pkgdatadir)
	cp -R javax $(DESTDIR)$(pkgdatadir)
	cp -R org $(DESTDIR)$(pkgdatadir)

# FIXME - should mimic doc/api/Makefile.am instead...
uninstall-local:
	rm -rf $(DESTDIR)$(pkgdatadir)/gnu
	rm -rf $(DESTDIR)$(pkgdatadir)/java
	rm -rf $(DESTDIR)$(pkgdatadir)/javax
	rm -rf $(DESTDIR)$(pkgdatadir)/org

endif # INSTALL_CLASS_FILES

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

dist-hook:
	mkdir -p $(distdir)
	cp -dfR $(top_srcdir)/gnu $(top_srcdir)/java $(top_srcdir)/javax $(top_srcdir)/org $(top_srcdir)/vm $(top_distdir)
# Delete not wanted files.
	$(FIND) $(top_distdir)/gnu $(top_distdir)/java $(top_distdir)/javax $(top_distdir)/org $(top_distdir)/vm -name CVS -print | xargs rm -fr
	$(FIND) $(top_distdir)/gnu $(top_distdir)/java $(top_distdir)/javax $(top_distdir)/org $(top_distdir)/vm -name .cvsignore -print | xargs rm -f
	$(FIND) $(top_distdir)/gnu $(top_distdir)/java $(top_distdir)/javax $(top_distdir)/org $(top_distdir)/vm -name class-dependencies.conf -print | xargs rm -f
	rm -f $(top_distdir)/gnu/classpath/Configuration.java
# Omit files listed in standard.omit.
	( cd $(top_distdir)/lib ; cat $(top_builddir)/lib/standard.omit | sed -e 's/\$$//' | sed -e 's/\/\.\*//' | xargs rm -fr )

