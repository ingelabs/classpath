#! /bin/sh
gen-classlist.sh standard
../../JavaDeps-2.0.1/jdeps --native -b "\$(JAVAC)" -d . -f classes -v > .deps
