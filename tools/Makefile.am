## Input file for automake to generate the Makefile.in used by configure

# Setup the compiler to use the GNU Classpath library we just build
if FOUND_GCJ
JCOMPILER = $(GCJ) --bootclasspath '$(top_builddir)/lib' --classpath . -C
else
if FOUND_JIKES
JCOMPILER = $(JIKES) -bootclasspath '' -extdirs '' -sourcepath '' --classpath $(top_builddir)/lib:.
else
if FOUND_GCJX
JCOMPILER = $(GCJX) -bootclasspath '' -sourcepath '' -classpath $(top_builddir)/lib:.
else
if FOUND_ECJ
JCOMPILER = $(ECJ) -bootclasspath '$(top_builddir)/lib' -classpath .
else
error dunno how to setup the JCOMPILER and compile
endif
endif
endif
endif

# All our example java source files
TOOLS_JAVA_FILES = $(srcdir)/gnu/classpath/tools/*.java $(srcdir)/gnu/classpath/tools/*/*.java $(srcdir)/gnu/classpath/tools/*/*/*.java


# The zip files with classes we want to produce.
TOOLS_ZIP = tools.zip

# Extra objects that will not exist until configure-time
BUILT_SOURCES = $(TOOLS_ZIP)

# the templates that must be included into the generated zip file
TOOLS_TEMPLATES = $(srcdir)/gnu/classpath/tools/giop/grmic/templates/*.jav
TOOLS_HELPS = $(srcdir)/gnu/classpath/tools/giop/*.txt

# The tool specific README files.
READMES = $(srcdir)/gnu/classpath/tools/giop/README

# All the files we find "interesting"
ALL_TOOLS_FILES = $(TOOLS_JAVA_FILES) $(TOOLS_TEMPLATES) $(READMES)

# Some architecture independent data to be installed.
TOOLS_DATA = $(TOOLS_ZIP)

# Where we want these data files installed.
TOOLSdir = $(pkgdatadir)/tools

# Make sure all sources and icons are also installed so users can use them.
# (Be careful to strip off the srcdir part of the path when installing.)
install-data-local:
	srcdir_cnt=`echo $(srcdir) | wc -c`; \
	for file in $(ALL_TOOLS_FILES); do \
	  f=`echo $$file | cut -c$$srcdir_cnt-`; \
	  fdir=`dirname $$f`; \
	  if test ! -d $(DESTDIR)$(pkgdatadir)/tools/$$fdir; then \
	    echo "$(mkinstalldirs) $(DESTDIR)$(pkgdatadir)/tools/$$fdir"; \
	    $(mkinstalldirs) $(DESTDIR)$(pkgdatadir)/tools/$$fdir; \
	  fi; \
	  echo "$(INSTALL_DATA) $$file $(DESTDIR)$(pkgdatadir)/tools/$$f"; \
	  $(INSTALL_DATA) $$file $(DESTDIR)$(pkgdatadir)/tools/$$f; \
	done

uninstall-local:
	srcdir_cnt=`echo $(srcdir) | wc -c`; \
	for file in $(ALL_TOOLS_FILES); do \
	  f=`echo $$file | cut -c$$srcdir_cnt-`; \
	  echo "rm -f $(DESTDIR)$(pkgdatadir)/tools/$$f"; \
	  rm -f $(DESTDIR)$(pkgdatadir)/tools/$$f; \
	done

# Make sure everything is included in the distribution.
EXTRA_DIST = Makefile.am
dist-hook:
	srcdir_cnt=`echo $(srcdir) | wc -c`; \
	for file in $(ALL_TOOLS_FILES); do \
	  f=`echo $$file | cut -c$$srcdir_cnt-`; \
	  fdir=`dirname $$f`; \
	  if test ! -d $(distdir)/$$fdir; then \
	    echo "$(makeinstalldirs) $(distdir)/$$fdir"; \
	    $(mkinstalldirs) $(distdir)/$$fdir; \
	  fi; \
	  echo "cp -p $$file $(distdir)/$$f"; \
	  cp -p $$file $(distdir)/$$f; \
	done

# To generate the example zip just depend on the sources and ignore the
# class files. Always regenerate all .class files and remove them immediatly.
# And copy the template files we use to the classes dir so they get also included.
$(TOOLS_ZIP): $(TOOLS_JAVA_FILES)
	mkdir -p classes/gnu/classpath/tools/giop/grmic/templates
	cp $(TOOLS_TEMPLATES) classes/gnu/classpath/tools/giop/grmic/templates
	cp $(TOOLS_HELPS) classes/gnu/classpath/tools/giop/
	$(JCOMPILER) -d classes $(TOOLS_JAVA_FILES) 
	(cd classes; \
	if test "$(ZIP)" != ""; then $(ZIP) -r ../$(TOOLS_ZIP) .; fi; \
	if test "$(FASTJAR)" != ""; then $(FASTJAR) cf ../$(TOOLS_ZIP) .; fi; \
	cd ..)
	rm -rf classes

# Zip file be gone! (and make sure the classes are gone too)
clean-local:
	rm -rf $(TOOLS_ZIP) classes
