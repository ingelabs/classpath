#!/bin/sh

set -e

LIBTOOLIZE=libtoolize
${LIBTOOLIZE} --version | head -n 1
echo "libtoolize: minimum version required: 1.5"

AUTOCONF=autoconf
${AUTOCONF} --version | head -n 1
echo "autoconf: minimum version required: 2.59"

# Autoheader is part of autoconf
AUTOHEADER=autoheader
${AUTOHEADER} --version | head -n 1
echo "autoheader: minimum version required: 2.59"

AUTOMAKE=automake
if test -x /usr/bin/automake-1.9; then
  AUTOMAKE=/usr/bin/automake-1.9
fi
${AUTOMAKE} --version | head -n 1
echo "automake: minimum version required: 1.9.0"

# Aclocal is part of automake
ACLOCAL=aclocal
if test -x /usr/bin/aclocal-1.9; then
  ACLOCAL=/usr/bin/aclocal-1.9
fi
${ACLOCAL} --version | head -n 1
echo "aclocal: minimum version required: 1.9.0"

echo "libtoolize ..."
${LIBTOOLIZE} --force --copy

echo "aclocal ..."
${ACLOCAL} -I .

echo "autoheader ..."
${AUTOHEADER} --force

echo "automake ..."
${AUTOMAKE} --add-missing --copy

echo "autoconf ..."
${AUTOCONF} --force

echo "Finished"
