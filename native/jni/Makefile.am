SUBDIRS = awt

pkglib_LTLIBRARIES = \
			libjavaio.la \
			libjavalang.la \
			libjavalangmath.la \
			libjavalangreflect.la \
			libjavanet.la \
			libjavautil.la

libjavaio_la_SOURCES = 	\
			javaio.c \
			javaio.h \
			java_io_File.c \
			java_io_FileDescriptor.c \
			java_io_FileInputStream.c \
			java_io_FileOutputStream.c \
			java_io_RandomAccessFile.c \
			java_io_ObjectOutputStream.c \
			java_io_ObjectInputStream.c

libjavalang_la_SOURCES = \
                        mprec.h \
                        mprec.c \
                        fdlibm.h \
			jcl.h \
			jcl.c \
			java_lang_System.c \
			java_lang_Object.c \
			java_lang_Float.c \
			java_lang_Double.c 

libjavalangmath_la_SOURCES = \
			acos.c asin.c atan.c atan2.c ceil.c \
			cos.c e_fmod.c exp.c floor.c k_cos.c \
			k_rem_pio2.c k_sin.c k_tan.c log.c \
			pow.c remainder.c rint.c s_fabs.c sin.c \
			sqrt.c tan.c fdlibm.h

libjavalangreflect_la_SOURCES = java_lang_reflect_Array.c

libjavanet_la_SOURCES = \
			javanet.c \
			javanet.h \
			java_net_InetAddress.c \
			java_net_PlainDatagramSocketImpl.c \
                        java_net_PlainSocketImpl.c

libjavautil_la_SOURCES = \
			java_util_TimeZone.c

libjavaio_la_LDFLAGS = -version-info @LIBVERSION@ 
libjavalang_la_LDFLAGS = -version-info @LIBVERSION@
libjavalangmath_la_LDFLAGS = -version-info @LIBVERSION@
libjavalangreflect_la_LDFLAGS = -version-info @LIBVERSION@
libjavanet_la_LDFLAGS = -version-info @LIBVERSION@
libjavautil_la_LDFLAGS = -version-info @LIBVERSION@

INCLUDES +=  @EXTRA_INCLUDES@ \
             -I$(top_srcdir)/native/lib/ \
	     -I$(top_builddir)/include 

clean-local:
	-rm -rf .deps
