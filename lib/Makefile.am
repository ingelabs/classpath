#if VMJAPHAR
#JAVAC = @JAPHAR_BIN@/japhar --classpath @JAPHAR_SHARE@/classes.jar sun.tools.javac.Main -classpath ../lib:@JAPHAR_SHARE@/classes.jar -d ../lib  
#else
JAVAC = java -classpath /usr/local/java/lib/classes.zip sun.tools.javac.Main -classpath ../lib:/usr/local/java/lib/classes.zip -d ../lib
#endif
JAVAHEADER = javah -classpath ../lib:/usr/local/java/lib/classes.zip -d ../lib

default: all
include .deps
all: $(CLASSES)

if MAINTAINER_BUILD
.deps: Makefile
	$(top_builddir)/lib/deps.sh
endif
