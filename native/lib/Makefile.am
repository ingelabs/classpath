
lib_LTLIBRARIES = libclasspath_lib.la

libclasspath_lib_la_SOURCES = jcl.c jcl.h jnilink.c jnilink.h primlib.c primlib.h

libclasspath_lib_la_LDFLAGS = -version-info 0:0:0

if JAPHAR
INCLUDES += -I../lib -I../nsa -I../vmi -I. -DNO_VMI @JAPHAR_CFLAGS@
endif
