bin_PROGRAMS = guile-jvm

if VMJAPHAR
INCLUDES = @GUILE_CFLAGS@ @JAPHAR_CFLAGS@
guile_jvm_LDADD = @GUILE_LDFLAGS@ @JAPHAR_LDFLAGS@ -ljni -ljaphar_lang -lruntime -larch -lpthread -lz
endif
