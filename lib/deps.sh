#! /bin/sh

find ../vm ../gnu ../java -type f -print | grep "\.java\$" > classes
../../JavaDeps-2.0.1/jdeps -b "\$(JAVAC)" -d lib -f classes -v > .deps
