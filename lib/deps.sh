#! /bin/sh
gen-classlist.sh standard
jdeps --native -b "\$(JAVAC)" -d . -f classes -v > .deps
