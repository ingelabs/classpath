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

## for JAVAH
## if USER_SPECIFIED_JAVAH
if USER_SPECIFIED_JAVAH
##   if USER_SPECIFIED_CLASSLIB
if USER_SPECIFIED_CLASSLIB
##     JAVAH = 
JAVAHEADER = @USER_JAVAH@ -classpath ../lib:@USER_CLASSLIB@ -d ../include
##   else
else
##     JAVAH = 
JAVAHEADER = @USER_JAVAH@ -classpath ../lib:/usr/local/java/lib/classes.zip -d ../include
##   endif
endif
## else
else 
##   if JAPHAR
if JAPHAR
##     JAVAH = 
JAVAHEADER = @JAPHAR_JAVAH@ --classpath ../lib:@JAPHAR_CLASSLIB@ -d ../include  
##   else
else
##     if USER_SPECIFIED_CLASSLIB
if USER_SPECIFIED_CLASSLIB
##       JAVAH = 
JAVAHEADER = javah -classpath ../lib:@USER_CLASSLIB@ -d ../include
##     else
else
##       JAVAH = 
JAVAHEADER = javah -classpath ../lib:/usr/local/java/lib/classes.zip -d ../include
##     endif
endif
##   endif
endif
## endif
endif

#data_DATA = glibj.zip
install-data-local :
	unzip -d $(datadir) glibj.zip

#default: all
include .deps
#all: .deps $(CLASSES) $(STUBS) glibj.zip

if DEVELOPER_BUILD
glibj.zip: .deps $(CLASSES) Makefile
	cp $(top_srcdir)/gnu/java/locale/*.properties gnu/java/locale/
	cp $(top_srcdir)/gnu/java/locale/*.uni gnu/java/locale/
	zip -r -D glibj.zip gnu java
endif

if MAINTAINER_BUILD
glibj.zip: .deps $(CLASSES) Makefile
	cp $(top_srcdir)/gnu/java/locale/*.properties gnu/java/locale/
	cp $(top_srcdir)/gnu/java/locale/*.uni gnu/java/locale/
	zip -r -D glibj.zip gnu java

.deps:	Makefile deps.sh gen-classlist.sh
	./deps.sh
endif

EXTRA_DIST = glibj.zip .deps
## not sure if we want to let the user easily get rid of glibj.zip
## CLEANFILES = glibj.zip
DISTCLEANFILES = $(CLASSES)
MAINTAINERCLEANFILES = .deps

