# Generated automatically from Makefile.in by configure.
# Makefile.in generated automatically by automake 1.4 from Makefile.am

# Copyright (C) 1994, 1995-8, 1999 Free Software Foundation, Inc.
# This Makefile.in is free software; the Free Software Foundation
# gives unlimited permission to copy and/or distribute it,
# with or without modifications, as long as this notice is preserved.

# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY, to the extent permitted by law; without
# even the implied warranty of MERCHANTABILITY or FITNESS FOR A
# PARTICULAR PURPOSE.

# used by automake to generate Makefile.in


SHELL = /bin/sh

srcdir = .
top_srcdir = ../../../../..
prefix = /usr/local/japhar
exec_prefix = ${prefix}

bindir = /usr/local/japhar/bin
sbindir = ${exec_prefix}/sbin
libexecdir = ${exec_prefix}/libexec
datadir = /usr/local/japhar/share
sysconfdir = ${prefix}/etc
sharedstatedir = ${prefix}/com
localstatedir = ${prefix}/var
libdir = ${exec_prefix}/lib
infodir = ${prefix}/info
mandir = ${prefix}/man
includedir = ${prefix}/include
oldincludedir = /usr/include

DESTDIR =

pkgdatadir = $(datadir)/classpath
pkglibdir = $(libdir)/classpath
pkgincludedir = $(includedir)/classpath

top_builddir = ../../../../..

ACLOCAL = aclocal
AUTOCONF = autoconf
AUTOMAKE = automake
AUTOHEADER = autoheader

INSTALL = /usr/bin/install -c
INSTALL_PROGRAM = ${INSTALL} $(AM_INSTALL_PROGRAM_FLAGS)
INSTALL_DATA = ${INSTALL} -m 644
INSTALL_SCRIPT = ${INSTALL_PROGRAM}
transform = s,x,x,

NORMAL_INSTALL = :
PRE_INSTALL = :
POST_INSTALL = :
NORMAL_UNINSTALL = :
PRE_UNINSTALL = :
POST_UNINSTALL = :
host_alias = i586-pc-linux-gnu
host_triplet = i586-pc-linux-gnu
AS = @AS@
AWK = gawk
CC = gcc
CPP = gcc -E
DLLTOOL = @DLLTOOL@
FIND = /usr/bin/find
GLIB_CFLAGS = -I/usr/lib/glib/include -D_REENTRANT
GLIB_CONFIG = /usr/bin/glib-config
GLIB_LIBS = -L/usr/lib -lgthread -lglib -lpthread
GTK_CFLAGS = -I/usr/X11R6/include -I/usr/lib/glib/include
GTK_CONFIG = /usr/bin/gtk-config
GTK_LIBS = -L/usr/lib -L/usr/X11R6/lib -lgtk -lgdk -rdynamic -lgmodule -lglib -ldl -lXext -lX11 -lm
JAPHAR_CFLAGS = -I/usr/local/japhar/include -I/usr/local/japhar/include/japhar
JAPHAR_CLASSLIB = /usr/local/japhar/share/classes.zip
JAPHAR_CONFIG = /usr/local/japhar/bin/japhar-config
JAPHAR_JABBA = /usr/local/japhar/bin/japhar
JAPHAR_JAVAC = /usr/local/japhar/bin/javac
JAPHAR_JAVAH = /usr/local/japhar/bin/javah
JAPHAR_LIBS = -L/usr/local/japhar/lib -ldl -lpthread -lm  -ljni -lruntime -lffi -larch -lz
JAPHAR_PREFIX = /usr/local/japhar
JIKES = /usr/bin/jikes
JVM = yes
LD = /usr/bin/ld
LIBART_CFLAGS = 
LIBART_CONFIG = /usr/bin/libart-config
LIBART_LIBS = -L/usr/lib -lart_lgpl -lm
LIBTOOL = $(SHELL) $(top_builddir)/libtool
LIBVERSION = 0:0
LN_S = ln -s
MAKEINFO = makeinfo
NM = /usr/bin/nm -B
OBJDUMP = @OBJDUMP@
PACKAGE = classpath
PERL = /usr/bin/perl
RANLIB = ranlib
REMOVE = 
SH = /bin/sh
USER_CLASSLIB = /opt/jdk/lib/classes.zip
USER_JABBA = 
USER_JAVAH = /opt/jdk/bin/javah
VERSION = 0.01
ZIP = /usr/bin/zip
prefix = /usr/local/japhar

EXTRA_DIST =  	GdkGraphics.java 	GtkButtonPeer.java 	GtkCanvasPeer.java 	GtkCheckboxMenuItemPeer.java 	GtkCheckboxPeer.java 	GtkChoicePeer.java 	GtkClipboard.java 	GtkComponentPeer.java 	GtkContainerPeer.java 	GtkDialogPeer.java 	GtkFileDialogPeer.java 	GtkFramePeer.java 	GtkGenericPeer.java 	GtkImage.java 	GtkImagePainter.java 	GtkLabelPeer.java 	GtkListPeer.java 	GtkMainThread.java 	GtkMenuBarPeer.java 	GtkMenuComponentPeer.java 	GtkMenuItemPeer.java 	GtkMenuPeer.java 	GtkPanelPeer.java 	GtkScrollPanePeer.java 	GtkScrollbarPeer.java 	GtkTextAreaPeer.java 	GtkTextComponentPeer.java 	GtkTextFieldPeer.java 	GtkToolkit.java 	GtkWindowPeer.java

mkinstalldirs = $(SHELL) $(top_srcdir)/mkinstalldirs
CONFIG_HEADER = ../../../../../include/config.h
CONFIG_CLEAN_FILES = 
DIST_COMMON =  Makefile.am Makefile.in


DISTFILES = $(DIST_COMMON) $(SOURCES) $(HEADERS) $(TEXINFOS) $(EXTRA_DIST)

TAR = gtar
GZIP_ENV = --best
all: all-redirect
.SUFFIXES:
$(srcdir)/Makefile.in: Makefile.am $(top_srcdir)/configure.in $(ACLOCAL_M4) 
	cd $(top_srcdir) && $(AUTOMAKE) --gnu gnu/java/awt/peer/gtk/Makefile

Makefile: $(srcdir)/Makefile.in  $(top_builddir)/config.status $(BUILT_SOURCES)
	cd $(top_builddir) \
	  && CONFIG_FILES=$(subdir)/$@ CONFIG_HEADERS= $(SHELL) ./config.status

tags: TAGS
TAGS:


distdir = $(top_builddir)/$(PACKAGE)-$(VERSION)/$(subdir)

subdir = gnu/java/awt/peer/gtk

distdir: $(DISTFILES)
	here=`cd $(top_builddir) && pwd`; \
	top_distdir=`cd $(top_distdir) && pwd`; \
	distdir=`cd $(distdir) && pwd`; \
	cd $(top_srcdir) \
	  && $(AUTOMAKE) --include-deps --build-dir=$$here --srcdir-name=$(top_srcdir) --output-dir=$$top_distdir --gnu gnu/java/awt/peer/gtk/Makefile
	@for file in $(DISTFILES); do \
	  d=$(srcdir); \
	  if test -d $$d/$$file; then \
	    cp -pr $$/$$file $(distdir)/$$file; \
	  else \
	    test -f $(distdir)/$$file \
	    || ln $$d/$$file $(distdir)/$$file 2> /dev/null \
	    || cp -p $$d/$$file $(distdir)/$$file || :; \
	  fi; \
	done
info-am:
info: info-am
dvi-am:
dvi: dvi-am
check-am: all-am
check: check-am
installcheck-am:
installcheck: installcheck-am
install-exec-am:
install-exec: install-exec-am

install-data-am:
install-data: install-data-am

install-am: all-am
	@$(MAKE) $(AM_MAKEFLAGS) install-exec-am install-data-am
install: install-am
uninstall-am:
uninstall: uninstall-am
all-am: Makefile
all-redirect: all-am
install-strip:
	$(MAKE) $(AM_MAKEFLAGS) AM_INSTALL_PROGRAM_FLAGS=-s install
installdirs:


mostlyclean-generic:

clean-generic:

distclean-generic:
	-rm -f Makefile $(CONFIG_CLEAN_FILES)
	-rm -f config.cache config.log stamp-h stamp-h[0-9]*

maintainer-clean-generic:
mostlyclean-am:  mostlyclean-generic

mostlyclean: mostlyclean-am

clean-am:  clean-generic mostlyclean-am

clean: clean-am

distclean-am:  distclean-generic clean-am
	-rm -f libtool

distclean: distclean-am

maintainer-clean-am:  maintainer-clean-generic distclean-am
	@echo "This command is intended for maintainers to use;"
	@echo "it deletes files that may require special tools to rebuild."

maintainer-clean: maintainer-clean-am

.PHONY: tags distdir info-am info dvi-am dvi check check-am \
installcheck-am installcheck install-exec-am install-exec \
install-data-am install-data install-am install uninstall-am uninstall \
all-redirect all-am all installdirs mostlyclean-generic \
distclean-generic clean-generic maintainer-clean-generic clean \
mostlyclean distclean maintainer-clean


# Tell versions [3.59,3.63) of GNU make to not export all variables.
# Otherwise a system limit (for SysV at least) may be exceeded.
.NOEXPORT:
