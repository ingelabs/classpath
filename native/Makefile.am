## Input file for automake to generate the Makefile.in used by configure

if CREATE_JNI_LIBRARIES

if CREATE_CNI_LIBRARIES
SUBDIRS = fdlibm cni jni
else
SUBDIRS = fdlibm jni 
endif

else
if CREATE_CNI_LIBRARIES
SUBDIRS = fdlibm cni
endif
endif

