if USER_SPECIFIED_JABBA
if USER_SPECIFIED_CLASSLIB
JAVAC = @USER_JABBA@ -classpath @USER_CLASSLIB@ sun.tools.javac.Main -classpath ../lib:@USER_CLASSLIB@ -d ../lib
else
JAVAC = @USER_JABBA@ -classpath /usr/local/java/lib/classes.zip sun.tools.javac.Main -classpath ../lib:/usr/local/java/lib/classes.zip -d ../lib
endif
else 
if JAPHAR
JAVAC = @JAPHAR_JABBA@ --classpath @JAPHAR_CLASSLIB@ sun.tools.javac.Main -classpath ../lib:@JAPHAR_CLASSLIB@ -d ../lib  
JAVAH = @JAPHAR_JAVAH@ -classpath ../lib:@JAPHAR_CLASSLIB@ -d ../lib
else
if USER_SPECIFIED_CLASSLIB
JAVAC = java -classpath @USER_CLASSLIB@ sun.tools.javac.Main -classpath ../lib:@USER_CLASSLIB@ -d ../lib
else
JAVAC = java -classpath /usr/local/java/lib/classes.zip sun.tools.javac.Main -classpath ../lib:/usr/local/java/lib/classes.zip -d ../lib
endif
JAVAH = javah -classpath ../lib:/usr/local/java/lib/classes.zip -d ../lib
endif
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

EXTRA_DIST = glibj.zip .deps
