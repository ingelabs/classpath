#!/bin/sh

set -e

LIBTOOLIZE=libtoolize
${LIBTOOLIZE} --version | head -1
echo "libtoolize: minimum version required: 1.4.2"

AUTOCONF=autoconf
${AUTOCONF} --version | head -1
echo "autoconf: minimum version required: 2.59"

# Autoheader is part of autoconf
AUTOHEADER=autoheader
${AUTOHEADER} --version | head -1
echo "autoheader: minimum version required: 2.59"

AUTOMAKE=automake
if test -x /usr/bin/automake-1.8; then
  AUTOMAKE=/usr/bin/automake-1.8
elif test -x /usr/bin/automake-1.7; then
  AUTOMAKE=/usr/bin/automake-1.7
fi
${AUTOMAKE} --version | head -1
echo "automake: minimum version required: 1.7.0"

# Aclocal is part of automake
ACLOCAL=aclocal
if test -x /usr/bin/aclocal-1.8; then
  ACLOCAL=/usr/bin/aclocal-1.8
elif test -x /usr/bin/aclocal-1.7; then
  ACLOCAL=/usr/bin/aclocal-1.7
fi
${ACLOCAL} --version | head -1
echo "aclocal: minimum version required: 1.7.0"

echo "libtoolize ..."
${LIBTOOLIZE} --force --copy

echo "aclocal ..."
${ACLOCAL}

echo "autoheader ..."
${AUTOHEADER} --force

echo "automake ..."
${AUTOMAKE} --add-missing --copy

echo "autoconf ..."
${AUTOCONF} --force

echo "Finished"
