JAVAC = java -classpath /usr/local/java/lib/classes.zip sun.tools.javac.Main -classpath ../lib:/usr/local/java/lib/classes.zip -d ../lib

default: all
include .deps
all: $(CLASSES)

.deps: Makefile
	$(top_builddir)/lib/deps.sh
