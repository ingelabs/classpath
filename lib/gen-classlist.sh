#!/bin/sh
find ../vm/reference ../java ../gnu -type f -print | grep '\.java$' > classes
for filexp in `cat $1.omit` ; do { grep -v ${filexp} < classes > classes.new ; mv classes.new classes ; } ; done
