if JAPHAR
JAVAC = @JAVA@ --classpath @CLASSLIB@ sun.tools.javac.Main -classpath ../lib:@CLASSLIB@ -d ../lib  
JAVAH = @JAVAH@ -classpath ../lib:@CLASSLIB@ -d ../lib
else
JAVAC = java -classpath /usr/local/java/lib/classes.zip sun.tools.javac.Main -classpath ../lib:/usr/local/java/lib/classes.zip -d ../lib
JAVAH = javah -classpath ../lib:/usr/local/java/lib/classes.zip -d ../lib
endif


default: all
include .deps
all: $(CLASSES) .deps glibj.zip

if MAINTAINER_BUILD
.deps:	Makefile deps.sh gen-classlist.sh
	./deps.sh
endif

glibj.zip: $(CLASSES)
	zip -r -D -0 glibj.zip gnu java

EXTRA_DIST: glibj.zip