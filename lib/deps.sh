#! /bin/sh

find ../vm ../gnu/java ../gnu/test ../gnu/vm ../java/beans ../java/io ../java/lang ../java/math ../java/net ../java/security ../java/util ../java/text -type f -print | grep "\.java\$" > classes
../../JavaDeps-2.0.1/jdeps --native -b "\$(JAVAC)" -d . -f classes -v > .deps
