#! /bin/sh

find ../vm ../gnu/java ../gnu/test ../gnu/vm ../java/beans ../java/io ../java/lang ../java/math ../java/net ../java/security ../java/util  -type f -print | grep "\.java\$" > classes
../../JavaDeps-2.0.1/jdeps -b "\$(JAVAC)" -d lib -f classes -v > .deps
