## used by automake to generate Makefile.in

EXTRA_DIST = \
classpath.security

# this may be a broken way to install, this file must be in 
# java.home/lib/security

install-data-local:
	$(mkinstalldirs) $(DESTDIR)$(libdir)/security
	$(INSTALL_DATA) $(srcdir)/classpath.security $(DESTDIR)$(libdir)/security/classpath.security

uninstall-local:
	rm -f $(DESTDIR)$(libdir)/security/classpath.security

