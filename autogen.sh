#!/bin/sh

set -e

libtoolize --version | head -1
echo "libtoolize: minimum version required: 1.4.2"
autoconf --version | head -1
echo "autoconf: minimum version required: 2.59"
automake --version | head -1
echo "automake: minimum version required: 1.7.0"

echo "libtoolize ..."
libtoolize --force --copy

echo "aclocal ..."
aclocal

echo "autoheader ..."
autoheader --force

echo "automake ..."
automake --add-missing --copy

echo "autoconf ..."
autoconf --force

echo "Finished"
