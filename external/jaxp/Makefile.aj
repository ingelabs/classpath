# Makefile for GNU classpathx "GNUJAXP" project
#
# You are free to redistribute this file. NO WARRANTY or fitness 
# for purpose is implied by this notice.
#
# !!!                                                  !!!
# !!!   MAKE EDITS TO "Makefile.in", not "Makefile"    !!!
# !!!                                                  !!!
#
# Use the "configure" command to generate the project Makefile
# from its "Makefile.in" template.
#

##set by configure

PROJECTROOT = @srcdir@

JAVAC = @JAVA_CC@
JAVAC_OPTS = -g @JAVA_CC_OPTS@
JAR = @JAR_TOOL@

SOURCEDIR = $(PROJECTROOT)/source
SOURCEFILES = $(SOURCEDIR)/gnu/xml/aelfred2/JAXPFactory.java \
	$(SOURCEDIR)/gnu/xml/aelfred2/SAXDriver.java \
	$(SOURCEDIR)/gnu/xml/aelfred2/XmlParser.java \
	$(SOURCEDIR)/gnu/xml/aelfred2/XmlReader.java \
	$(SOURCEDIR)/gnu/xml/dom/Consumer.java \
	$(SOURCEDIR)/gnu/xml/dom/DomAttr.java \
	$(SOURCEDIR)/gnu/xml/dom/DomCDATA.java \
	$(SOURCEDIR)/gnu/xml/dom/DomCharacterData.java \
	$(SOURCEDIR)/gnu/xml/dom/DomComment.java \
	$(SOURCEDIR)/gnu/xml/dom/DomDoctype.java \
	$(SOURCEDIR)/gnu/xml/dom/DomDocument.java \
	$(SOURCEDIR)/gnu/xml/dom/DomElement.java \
	$(SOURCEDIR)/gnu/xml/dom/DomEntity.java \
	$(SOURCEDIR)/gnu/xml/dom/DomEntityReference.java \
	$(SOURCEDIR)/gnu/xml/dom/DomEvent.java \
	$(SOURCEDIR)/gnu/xml/dom/DomEx.java \
	$(SOURCEDIR)/gnu/xml/dom/DomExtern.java \
	$(SOURCEDIR)/gnu/xml/dom/DomFragment.java \
	$(SOURCEDIR)/gnu/xml/dom/DomImpl.java \
	$(SOURCEDIR)/gnu/xml/dom/DomIterator.java \
	$(SOURCEDIR)/gnu/xml/dom/DomNamedNodeMap.java \
	$(SOURCEDIR)/gnu/xml/dom/DomNode.java \
	$(SOURCEDIR)/gnu/xml/dom/DomNotation.java \
	$(SOURCEDIR)/gnu/xml/dom/DomNsNode.java \
	$(SOURCEDIR)/gnu/xml/dom/DomPI.java \
	$(SOURCEDIR)/gnu/xml/dom/DomText.java \
	$(SOURCEDIR)/gnu/xml/dom/JAXPFactory.java \
	$(SOURCEDIR)/gnu/xml/pipeline/CallFilter.java \
	$(SOURCEDIR)/gnu/xml/pipeline/DomConsumer.java \
	$(SOURCEDIR)/gnu/xml/pipeline/EventConsumer.java \
	$(SOURCEDIR)/gnu/xml/pipeline/EventFilter.java \
	$(SOURCEDIR)/gnu/xml/pipeline/LinkFilter.java \
	$(SOURCEDIR)/gnu/xml/pipeline/NSFilter.java \
	$(SOURCEDIR)/gnu/xml/pipeline/PipelineFactory.java \
	$(SOURCEDIR)/gnu/xml/pipeline/TeeConsumer.java \
	$(SOURCEDIR)/gnu/xml/pipeline/TextConsumer.java \
	$(SOURCEDIR)/gnu/xml/pipeline/ValidationConsumer.java \
	$(SOURCEDIR)/gnu/xml/pipeline/WellFormednessFilter.java \
	$(SOURCEDIR)/gnu/xml/pipeline/XIncludeFilter.java \
	$(SOURCEDIR)/gnu/xml/pipeline/XsltFilter.java \
	$(SOURCEDIR)/gnu/xml/util/DoParse.java \
	$(SOURCEDIR)/gnu/xml/util/DomParser.java \
	$(SOURCEDIR)/gnu/xml/util/Resolver.java \
	$(SOURCEDIR)/gnu/xml/util/SAXNullTransformerFactory.java \
	$(SOURCEDIR)/gnu/xml/util/XCat.java \
	$(SOURCEDIR)/gnu/xml/util/XHTMLWriter.java \
	$(SOURCEDIR)/gnu/xml/util/XMLWriter.java \
	$(SOURCEDIR)/javax/xml/parsers/ClassStuff.java \
	$(SOURCEDIR)/javax/xml/parsers/DocumentBuilder.java \
	$(SOURCEDIR)/javax/xml/parsers/DocumentBuilderFactory.java \
	$(SOURCEDIR)/javax/xml/parsers/FactoryConfigurationError.java \
	$(SOURCEDIR)/javax/xml/parsers/ParserConfigurationException.java \
	$(SOURCEDIR)/javax/xml/parsers/SAXParser.java \
	$(SOURCEDIR)/javax/xml/parsers/SAXParserFactory.java \
	$(SOURCEDIR)/javax/xml/transform/ClassStuff.java \
	$(SOURCEDIR)/javax/xml/transform/ErrorListener.java \
	$(SOURCEDIR)/javax/xml/transform/OutputKeys.java \
	$(SOURCEDIR)/javax/xml/transform/Result.java \
	$(SOURCEDIR)/javax/xml/transform/Source.java \
	$(SOURCEDIR)/javax/xml/transform/SourceLocator.java \
	$(SOURCEDIR)/javax/xml/transform/Templates.java \
	$(SOURCEDIR)/javax/xml/transform/Transformer.java \
	$(SOURCEDIR)/javax/xml/transform/TransformerConfigurationException.java \
	$(SOURCEDIR)/javax/xml/transform/TransformerException.java \
	$(SOURCEDIR)/javax/xml/transform/TransformerFactory.java \
	$(SOURCEDIR)/javax/xml/transform/TransformerFactoryConfigurationError.java \
	$(SOURCEDIR)/javax/xml/transform/URIResolver.java \
	$(SOURCEDIR)/javax/xml/transform/dom/DOMLocator.java \
	$(SOURCEDIR)/javax/xml/transform/dom/DOMResult.java \
	$(SOURCEDIR)/javax/xml/transform/dom/DOMSource.java \
	$(SOURCEDIR)/javax/xml/transform/sax/SAXResult.java \
	$(SOURCEDIR)/javax/xml/transform/sax/SAXSource.java \
	$(SOURCEDIR)/javax/xml/transform/sax/SAXTransformerFactory.java \
	$(SOURCEDIR)/javax/xml/transform/sax/TemplatesHandler.java \
	$(SOURCEDIR)/javax/xml/transform/sax/TransformerHandler.java \
	$(SOURCEDIR)/javax/xml/transform/stream/StreamResult.java \
	$(SOURCEDIR)/javax/xml/transform/stream/StreamSource.java \
	$(SOURCEDIR)/org/w3c/dom/Attr.java \
	$(SOURCEDIR)/org/w3c/dom/CDATASection.java \
	$(SOURCEDIR)/org/w3c/dom/CharacterData.java \
	$(SOURCEDIR)/org/w3c/dom/Comment.java \
	$(SOURCEDIR)/org/w3c/dom/DOMException.java \
	$(SOURCEDIR)/org/w3c/dom/DOMImplementation.java \
	$(SOURCEDIR)/org/w3c/dom/Document.java \
	$(SOURCEDIR)/org/w3c/dom/DocumentFragment.java \
	$(SOURCEDIR)/org/w3c/dom/DocumentType.java \
	$(SOURCEDIR)/org/w3c/dom/Element.java \
	$(SOURCEDIR)/org/w3c/dom/Entity.java \
	$(SOURCEDIR)/org/w3c/dom/EntityReference.java \
	$(SOURCEDIR)/org/w3c/dom/NamedNodeMap.java \
	$(SOURCEDIR)/org/w3c/dom/Node.java \
	$(SOURCEDIR)/org/w3c/dom/NodeList.java \
	$(SOURCEDIR)/org/w3c/dom/Notation.java \
	$(SOURCEDIR)/org/w3c/dom/ProcessingInstruction.java \
	$(SOURCEDIR)/org/w3c/dom/Text.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSS2Azimuth.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSS2BackgroundPosition.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSS2BorderSpacing.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSS2CounterIncrement.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSS2CounterReset.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSS2Cursor.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSS2FontFaceSrc.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSS2FontFaceWidths.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSS2PageSize.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSS2PlayDuring.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSS2Properties.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSS2TextShadow.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSCharsetRule.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSFontFaceRule.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSImportRule.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSMediaRule.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSPageRule.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSPrimitiveValue.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSRule.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSRuleList.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSStyleDeclaration.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSStyleRule.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSStyleSheet.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSUnknownRule.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSValue.java \
	$(SOURCEDIR)/org/w3c/dom/css/CSSValueList.java \
	$(SOURCEDIR)/org/w3c/dom/css/Counter.java \
	$(SOURCEDIR)/org/w3c/dom/css/DOMImplementationCSS.java \
	$(SOURCEDIR)/org/w3c/dom/css/DocumentCSS.java \
	$(SOURCEDIR)/org/w3c/dom/css/ElementCSSInlineStyle.java \
	$(SOURCEDIR)/org/w3c/dom/css/RGBColor.java \
	$(SOURCEDIR)/org/w3c/dom/css/Rect.java \
	$(SOURCEDIR)/org/w3c/dom/css/ViewCSS.java \
	$(SOURCEDIR)/org/w3c/dom/events/DocumentEvent.java \
	$(SOURCEDIR)/org/w3c/dom/events/Event.java \
	$(SOURCEDIR)/org/w3c/dom/events/EventException.java \
	$(SOURCEDIR)/org/w3c/dom/events/EventListener.java \
	$(SOURCEDIR)/org/w3c/dom/events/EventTarget.java \
	$(SOURCEDIR)/org/w3c/dom/events/MouseEvent.java \
	$(SOURCEDIR)/org/w3c/dom/events/MutationEvent.java \
	$(SOURCEDIR)/org/w3c/dom/events/UIEvent.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLAnchorElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLAppletElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLAreaElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLBRElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLBaseElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLBaseFontElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLBodyElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLButtonElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLCollection.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLDListElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLDOMImplementation.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLDirectoryElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLDivElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLDocument.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLFieldSetElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLFontElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLFormElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLFrameElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLFrameSetElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLHRElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLHeadElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLHeadingElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLHtmlElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLIFrameElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLImageElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLInputElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLIsIndexElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLLIElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLLabelElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLLegendElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLLinkElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLMapElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLMenuElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLMetaElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLModElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLOListElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLObjectElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLOptGroupElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLOptionElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLParagraphElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLParamElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLPreElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLQuoteElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLScriptElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLSelectElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLStyleElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLTableCaptionElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLTableCellElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLTableColElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLTableElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLTableRowElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLTableSectionElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLTextAreaElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLTitleElement.java \
	$(SOURCEDIR)/org/w3c/dom/html/HTMLUListElement.java \
	$(SOURCEDIR)/org/w3c/dom/ranges/DocumentRange.java \
	$(SOURCEDIR)/org/w3c/dom/ranges/Range.java \
	$(SOURCEDIR)/org/w3c/dom/ranges/RangeException.java \
	$(SOURCEDIR)/org/w3c/dom/stylesheets/DocumentStyle.java \
	$(SOURCEDIR)/org/w3c/dom/stylesheets/LinkStyle.java \
	$(SOURCEDIR)/org/w3c/dom/stylesheets/MediaList.java \
	$(SOURCEDIR)/org/w3c/dom/stylesheets/StyleSheet.java \
	$(SOURCEDIR)/org/w3c/dom/stylesheets/StyleSheetList.java \
	$(SOURCEDIR)/org/w3c/dom/traversal/DocumentTraversal.java \
	$(SOURCEDIR)/org/w3c/dom/traversal/NodeFilter.java \
	$(SOURCEDIR)/org/w3c/dom/traversal/NodeIterator.java \
	$(SOURCEDIR)/org/w3c/dom/traversal/TreeWalker.java \
	$(SOURCEDIR)/org/w3c/dom/views/AbstractView.java \
	$(SOURCEDIR)/org/w3c/dom/views/DocumentView.java \
	$(SOURCEDIR)/org/xml/sax/AttributeList.java \
	$(SOURCEDIR)/org/xml/sax/Attributes.java \
	$(SOURCEDIR)/org/xml/sax/ContentHandler.java \
	$(SOURCEDIR)/org/xml/sax/DTDHandler.java \
	$(SOURCEDIR)/org/xml/sax/DocumentHandler.java \
	$(SOURCEDIR)/org/xml/sax/EntityResolver.java \
	$(SOURCEDIR)/org/xml/sax/ErrorHandler.java \
	$(SOURCEDIR)/org/xml/sax/HandlerBase.java \
	$(SOURCEDIR)/org/xml/sax/InputSource.java \
	$(SOURCEDIR)/org/xml/sax/Locator.java \
	$(SOURCEDIR)/org/xml/sax/Parser.java \
	$(SOURCEDIR)/org/xml/sax/SAXException.java \
	$(SOURCEDIR)/org/xml/sax/SAXNotRecognizedException.java \
	$(SOURCEDIR)/org/xml/sax/SAXNotSupportedException.java \
	$(SOURCEDIR)/org/xml/sax/SAXParseException.java \
	$(SOURCEDIR)/org/xml/sax/XMLFilter.java \
	$(SOURCEDIR)/org/xml/sax/XMLReader.java \
	$(SOURCEDIR)/org/xml/sax/ext/Attributes2.java \
	$(SOURCEDIR)/org/xml/sax/ext/Attributes2Impl.java \
	$(SOURCEDIR)/org/xml/sax/ext/DeclHandler.java \
	$(SOURCEDIR)/org/xml/sax/ext/DefaultHandler2.java \
	$(SOURCEDIR)/org/xml/sax/ext/EntityResolver2.java \
	$(SOURCEDIR)/org/xml/sax/ext/LexicalHandler.java \
	$(SOURCEDIR)/org/xml/sax/ext/Locator2.java \
	$(SOURCEDIR)/org/xml/sax/ext/Locator2Impl.java \
	$(SOURCEDIR)/org/xml/sax/helpers/AttributeListImpl.java \
	$(SOURCEDIR)/org/xml/sax/helpers/AttributesImpl.java \
	$(SOURCEDIR)/org/xml/sax/helpers/DefaultHandler.java \
	$(SOURCEDIR)/org/xml/sax/helpers/LocatorImpl.java \
	$(SOURCEDIR)/org/xml/sax/helpers/NamespaceSupport.java \
	$(SOURCEDIR)/org/xml/sax/helpers/NewInstance.java \
	$(SOURCEDIR)/org/xml/sax/helpers/ParserAdapter.java \
	$(SOURCEDIR)/org/xml/sax/helpers/ParserFactory.java \
	$(SOURCEDIR)/org/xml/sax/helpers/XMLFilterImpl.java \
	$(SOURCEDIR)/org/xml/sax/helpers/XMLReaderAdapter.java \
	$(SOURCEDIR)/org/xml/sax/helpers/XMLReaderFactory.java

PKGFILES = \
	$(SOURCEDIR)/gnu/xml/aelfred2/package.html \
	$(SOURCEDIR)/gnu/xml/dom/package.html \
	$(SOURCEDIR)/gnu/xml/pipeline/package.html \
	$(SOURCEDIR)/gnu/xml/util/package.html \
	$(SOURCEDIR)/javax/xml/parsers/package.html \
	$(SOURCEDIR)/javax/xml/transform/package.html \
	$(SOURCEDIR)/javax/xml/transform/dom/package.html \
	$(SOURCEDIR)/javax/xml/transform/sax/package.html \
	$(SOURCEDIR)/javax/xml/transform/stream/package.html \
	$(SOURCEDIR)/org/w3c/dom/package.html \
	$(SOURCEDIR)/org/w3c/dom/events/package.html \
	$(SOURCEDIR)/org/w3c/dom/traversal/package.html \
	$(SOURCEDIR)/org/w3c/dom/views/package.html \
	$(SOURCEDIR)/org/xml/sax/package.html \
	$(SOURCEDIR)/org/xml/sax/ext/package.html \
	$(SOURCEDIR)/org/xml/sax/helpers/package.html \


#GCJ options.
GCJ_OPTS = --encoding=8859_1 -fsyntax-only -femit-class-files 
GCJ_COMPILE = $(GCJ) $(GCJ_OPTS) -Isource -foutput-class-dir=$(CC-DESTDIR)

# Variables which define some useful constants
newline:=\\n
empty:=
space:=$(empty) $(empty)


default: gnujaxp.jar

all: gnujaxp.jar javadoc


# Ensure the makefile can update itself.
Makefile: Makefile.in configure.in
	   $(SHELL) ./config.status

Makefile.in: Makefile.aj
	     $(SHELL) $(PROJECTROOT)/automakejar ./Makefile.in


# Build the META-INF directory.
META-INF META-INF/services:
	mkdir $@

# Admin file targets.
META-INF/COPYING: META-INF $(PROJECTROOT)/COPYING
	cp $(PROJECTROOT)/COPYING $@

META-INF/LICENSE: META-INF $(PROJECTROOT)/LICENSE
	cp $(PROJECTROOT)/LICENSE $@

# Support file targets.
META-INF/services/org.xml.sax.driver: META-INF/services
	echo -n gnu.xml.aelfred2.XmlReader > $@

META-INF/services/javax.xml.parsers.SAXParserFactory: META-INF/services
	echo -n gnu.xml.aelfred2.JAXPFactory > $@

META-INF/services/javax.xml.parsers.DocumentBuilderFactory: META-INF/services
	echo -n gnu.xml.dom.JAXPFactory > $@

SUPPORTFILES = \
		META-INF/COPYING \
		META-INF/LICENSE \
		META-INF/services/org.xml.sax.driver \
		META-INF/services/javax.xml.parsers.SAXParserFactory \
		META-INF/services/javax.xml.parsers.DocumentBuilderFactory 


# This is an automakejar target.
# You must run the automakejar script on this Make file to
# cause the target to be legal Make syntax.
gnujaxp.jar:
	sourcedir=$(SOURCEDIR)
	sourcefiles=$(SOURCEFILES)
	classpath=$(wildcard lib/*.jar)
	classesdest=classes
	otherfiles=$(SUPPORTFILES)
	manifest=$(PROJECTROOT)/manifest.mf
# End of automakejar target.



# for normal development
# must rerun "make" after this
clean:
	-rm -rf gnujaxp.jar classes META-INF Log apidoc apidoc.zip
	-rm -rf gnujaxp-*.zip

# Nust rerun automakejar and configure after this
distclean:	clean
	-rm -f Makefile Makefile.in config.cache config.log config.status

# Nust rerun aclocal, autoconf, automakejar and configure after this
mrproper:	distclean
	-rm -f configure

javadoc: apidoc.zip
apidoc.zip:
	-rm -rf apidoc
	mkdir -p apidoc
	javadoc -d apidoc \
	    -windowtitle "GNU JAXP Library" \
	    -nodeprecatedlist \
	    -version -author -use \
	    -bottom "<p>Source code is under GPL \
		(with library exception) in the JAXP project at \
		<a href='http://www.gnu.org/software/classpathx/jaxp'> \
		http://www.gnu.org/software/classpathx/jaxp</a> \
		<br>This documentation was derived from that\
		source code on `date -I`.\
		</p>" \
	    -classpath "$(SOURCEDIR)" \
	    \
	    -group "AElfred2 and SAX2 Utilities" \
	    	"gnu.xml.aelfred2:gnu.xml.pipeline:gnu.xml.util" \
	    -group "DOM2, implementing DOM Level 2" \
	    	"gnu.xml.dom" \
	    \
	    -group "SAX and SAX2" \
	    	"org.xml.sax:org.xml.sax.ext:org.xml.sax.helpers" \
	    -group "DOM Level 2 (W3C)" \
	    	"org.w3c.*" \
	    -group "Java API for XML (v1.1) (Sun)" \
	    	"javax.xml.*" \
	    \
	    gnu.xml.aelfred2 gnu.xml.pipeline gnu.xml.util \
	    gnu.xml.dom \
	    org.xml.sax org.xml.sax.ext org.xml.sax.helpers \
	    org.w3c.dom org.w3c.dom.events \
	    javax.xml.parsers \
	    javax.xml.transform javax.xml.transform.dom \
	    javax.xml.transform.sax javax.xml.transform.stream
	jar cMf apidoc.zip apidoc

#
# RELEASE ENGINEERING:
# "full" release has source, jarfile, javadoc, and extras
#
# override VERSION in environment, like:
#  $ VERSION=1.0beta1 make -e release
#
VERSION =	dev

release: gnujaxp-$(VERSION).zip


DIST_DIR =	gnujaxp-$(VERSION)

# XXX ChangeLog
FILES		:= \
	LICENSE COPYING COPYRIGHT.html README \
	gnujaxp.jar \
	configure Makefile.in manifest.mf 

gnujaxp-$(VERSION).zip: $(FILES) apidoc.zip $(SOURCEFILES) $(PKGFILES)
	for F in $(FILES) $(SOURCEFILES) $(PKGFILES) ;\
	do \
	    mkdir -p `dirname $(DIST_DIR)/$$F` ;\
	    cp $$F $(DIST_DIR)/$$F ;\
	done
	cd $(DIST_DIR); jar xf ../apidoc.zip
	chmod +x $(DIST_DIR)/configure
	cp gnujaxp.jar $(DIST_DIR)
	# jar cMf gnujaxp-$(VERSION).zip $(DIST_DIR)
	zip -qr9 gnujaxp-$(VERSION).zip $(DIST_DIR)
	rm -rf $(DIST_DIR)

