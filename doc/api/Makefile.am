if CREATE_API_DOCS
noinst_DATA = html
endif

EXTRA_DIST = classpath-copyright.xml

htmllist := $(shell $(FIND) html | grep -v "^html$$")

install-data-local:
	$(mkinstalldirs) $(DESTDIR)$(pkgdatadir)/api
	@list='$(htmllist)'; for p in $$list; do \
	  f="`echo $$p | sed -e 's|^.*/||'`"; \
	  if test -f "$$p"; then \
	    echo " $(INSTALL_DATA) $$p $(DESTDIR)$(pkgdatadir)/api/$$f"; \
	    $(INSTALL_DATA) $$p $(DESTDIR)$(pkgdatadir)/api/$$f; \
	  elif test -d "$$p"; then \
	    $(mkinstalldirs) $(DESTDIR)$(pkgdatadir)/api/$$f; \
          fi; \
        done

uninstall-local:
	@list='$(htmllist)'; for p in $$list; do \
	  f="`echo $$p | sed -e 's|^.*/||'`"; \
	  if test -f "$$p"; then \
	    echo " rm -f $(DESTDIR)$(pkgdatadir)/api/$$f"; \
	    rm -f $(DESTDIR)$(pkgdatadir)/api/$$f; \
          fi; \
        done

#html: create_xml create_catalog create_html
html: create_html

core := $(shell cd $(top_srcdir) && $(FIND) java javax -type d ! -regex .*CVS ! -regex .*doc-files | tr '/' '.' | grep -v "java.awt.dnd.peer" | grep -v "java.awt.peer")
jaxp := $(shell cd $(top_srcdir)/external/jaxp/source && $(FIND) javax org -type d ! -regex .*CVS | tr '/' '.' | grep -v "org.w3c.dom.")

# Just generate api doc for the core packages.
# Adding the external ones takes a lot more time and we should reference
# them anyway since we will place out own copyright notive on the generated
# pages.
# packages = $(core) $(jaxp)
packages = $(core)
#| grep -v "^java\$" )
date := $(shell $(DATE))

clean:
#	-rm -rf xml
	-rm -rf html
#	-rm -f gjdoccatalog.xml
#	-rm -f gjdoc_rawcomment.cache

#create_xml:
#	-$(MKDIR) xml > /dev/null 2>&1
#	$(GJDOC) -sourcepath \
#		"$(top_srcdir):$(top_srcdir)/external/jaxp/source:$(top_srcdir)/vm/reference" \
#		-doclet gnu.classpath.tools.doclets.xmldoclet.Driver \
#		-d xml $(packages) 
#	-touch create_xml

#create_catalog:
#	$(XMLCATALOG) --noout --create \
#		--add "public" \
#		"-//GNU//DTD Gjdoc XML V0.1.1//EN" \
#		http://www.gnu.org/software/cp-tools/dtd/gjdoc.dtd \
#		--add "rewriteSystem" \
#		http://www.gnu.org/software/cp-tools/dtd/ \
#		file:///usr/local/share/gjdoc/dtd/ \
#		gjdoccatalog.xml 
#	-touch create_catalog

create_html:
	-$(MKDIR) html > /dev/null 2>&1
	$(GJDOC) -sourcepath "$(top_srcdir):$(top_srcdir)/vm/reference" \
	-d html -bottomnote "$(srcdir)/classpath-copyright.xml" \
	$(packages)

# Including the external ones (jaxp) this would be:
# create_html:
# 	-$(MKDIR) html > /dev/null 2>&1
# 	$(GJDOC) -sourcepath \
# 	"$(top_srcdir):$(top_srcdir)/vm/reference:$(top_srcdir)/external/jaxp/source" \
# 	-d html -bottomnote "$(srcdir)/classpath-copyright.xml" \
# 	$(packages)

#	-$(CP) -Ru $(srcdir)/xslt/common html
#	$(XSLTPROC) \
#		--novalid \
#		--param verbose "1" \
#		--param now "'$(date)'" \
#		--param copyrightfile "'$(srcdir)/classpath-copyright.xml'" \
#		--param windowtitle "'GNU Classpath CVS'" \
#		--param targetdir "'html'" \
#		"$(srcdir)/xslt/gjdocxml2html.xsl" \
#		"xml/index.xml"
	-touch create_html

