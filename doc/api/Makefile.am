if CREATE_API_DOCS
all: Makefile create_xml create_catalog create_html
else
all: Makefile
endif

.PHONY: create_xml create_catalog create_html

packages := $(shell cd $(top_srcdir) && $(FIND) java javax -type d ! -regex .*CVS | tr '/' '.' )
#| grep -v "^java\$" )
date := $(shell $(DATE))

clean:
	-rm -rf xml
	-rm -rf html
	-rm -f gjdoccatalog.xml
	-rm -f gjdoc_rawcomment.cache

create_xml:
	-mkdir xml
	$(GJDOC) -sourcepath \
		"$(top_srcdir):$(top_srcdir)/vm/reference" \
		-doclet gnu.classpath.tools.doclets.xmldoclet.Driver \
		-d xml $(packages) 

create_catalog:
	$(XMLCATALOG) --noout --create \
		--add "public" \
		"-//GNU//DTD Gjdoc XML V0.1.1//EN" \
		http://www.gnu.org/software/cp-tools/dtd/gjdoc.dtd \
		--add "rewriteSystem" \
		http://www.gnu.org/software/cp-tools/dtd/ \
		file:///usr/local/share/gjdoc/dtd/ \
		gjdoccatalog.xml 

create_html:
	-mkdir html
	-$(CP) -Ru $(srcdir)/xslt/common html
	$(XSLTPROC) \
		--novalid \
		--param verbose "1" \
		--param now "'$(date)'" \
		--param copyrightfile "'$(srcdir)/classpath-copyright.xml'" \
		--param windowtitle "'GNU Classpath CVS'" \
		--param targetdir "'html'" \
		"$(srcdir)/xslt/gjdocxml2html.xsl" \
		"xml/index.xml"

