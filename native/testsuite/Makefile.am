all: guile-jvm

CLEANFILES = guile-jvm

bin_PROGS: guile-jvm

guile-jvm:	guile-jvm.c
	@CC@ guile-jvm.c $(EXTRA_INCLUDES) $(EXTRA_LIBS) $(EXTRA_CFLAGS) -o guile-jvm

EXTRA_CFLAGS= -ljni -ljaphar_lang -lruntime -larch -lguile -lreadline -lpthread -lz -ldl
EXTRA_LIBS=-L@VMLIB@
EXTRA_INCLUDES=-I/usr/local/include/libguile -I@VMINCLUDE@ -I@VMINCLUDE@/private

# need to add GUILE_INCLUDE to configure.in
