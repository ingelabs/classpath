
lib_LTLIBRARIES = libjavalangreflect.la

libjavalangreflect_la_SOURCES = Array.c reflect.c

libjavalangreflect_la_LDFLAGS = -version-info 0:0:0

if JAPHAR
INCLUDES += -I ../lib -I ../vmi -I ../nsa -DNO_VMI @JAPHAR_CFLAGS@
endif
