## Input file for automake to generate the Makefile.in used by configure

if CREATE_JNI_LIBRARIES

if CREATE_CNI_LIBRARIES
SUBDIRS = fdlibm cni jni target
else
SUBDIRS = fdlibm jni target
endif

else
if CREATE_CNI_LIBRARIES
SUBDIRS = fdlibm cni target
endif
endif

