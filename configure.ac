dnl Process this file with autoconf to produce a configure script.

dnl Turning off cache for debug reasons
dnl define([AC_CACHE_LOAD], )dnl
dnl define([AC_CACHE_SAVE], )dnl

AC_INIT([GNU Classpath],[0.07+cvs],[classpath@gnu.org],[classpath])
AC_CONFIG_SRCDIR(java/lang/System.java)

AC_CANONICAL_TARGET
if expr match ${target_cpu} .*86 > /dev/null; then target_cpu=x86; fi

dnl We will not track/change lib version until we reach version 1.0
dnl at which time we'll have to be more anal about such things
AC_SUBST(LIBVERSION, "0:0:0")

AC_PREREQ(2.59)
AM_INIT_AUTOMAKE(1.7.0)
AC_CONFIG_HEADERS([include/config.h])
AC_PREFIX_DEFAULT(/usr/local/classpath)

AC_ARG_ENABLE([java],
              [AS_HELP_STRING(--enable-java,compile Java source [default=yes])],
              [case "${enableval}" in 
                 yes) COMPILE_JAVA=yes ;;
                 no) COMPILE_JAVA=no ;;
                 *) COMPILE_JAVA=yes ;;
               esac],
              [COMPILE_JAVA=yes])
AM_CONDITIONAL(BUILD_GLIBJ_ZIP, test "x${COMPILE_JAVA}" = xyes)

AC_ARG_ENABLE([jni],
              [AS_HELP_STRING(--enable-jni,compile JNI source [default=yes])],
              [case "${enableval}" in 
                yes) COMPILE_JNI=yes; COMPILE_JAVA=yes ;;
                no) COMPILE_JNI=no ;;
                *) COMPILE_JNI=yes; COMPILE_JAVA=yes ;;
              esac],
              [COMPILE_JNI=yes])

AC_ARG_ENABLE([cni],
              [AS_HELP_STRING(--enable-cni,compile CNI source [default=no])],
              [case "${enableval}" in 
                yes) COMPILE_CNI=yes; COMPILE_JAVA=yes ;;
                no) COMPILE_CNI=no ;;
                *) COMPILE_CNI=yes; COMPILE_JAVA=yes ;;
              esac],
              [COMPILE_CNI=no])

dnl -----------------------------------------------------------
dnl GTK native peer
dnl -----------------------------------------------------------
AC_ARG_ENABLE([gtk-peer],
              [AS_HELP_STRING(--enable-gtk-peer,compile GTK native peers [default=yes])],
              [case "${enableval}" in
                yes) COMPILE_GTK_PEER=yes ;;
                no) COMPILE_GTK_PEER=no ;;
                *) COMPILE_GTK_PEER=yes ;;
              esac],
              [COMPILE_GTK_PEER=yes])

AM_CONDITIONAL(CREATE_JNI_LIBRARIES, test "x${COMPILE_JNI}" = xyes)
AM_CONDITIONAL(CREATE_CNI_LIBRARIES, test "x${COMPILE_CNI}" = xyes)
AM_CONDITIONAL(CREATE_GTK_PEER_LIBRARIES, test "x${COMPILE_GTK_PEER}" = xyes)

AC_ARG_ENABLE([regen-headers],
              [AS_HELP_STRING(--enable-regen-headers,automatically regenerate JNI headers [default=no])],
              [case "${enableval}" in
                yes) REGENERATE_JNI_HEADERS=yes ;;
                no) REGENERATE_JNI_HEADERS=no ;;
                *) REGENERATE_JNI_HEADERS=no ;;
              esac],
              [REGENERATE_JNI_HEADERS=no])

AM_CONDITIONAL(CREATE_JNI_HEADERS, test "x${REGENERATE_JNI_HEADERS}" = xyes)

AC_PROG_LN_S
AC_PROG_INSTALL

dnl Checks for programs.
dnl AC_PROG_CXX
dnl Initialize libtool
AC_DISABLE_STATIC
AC_PROG_LIBTOOL
dnl AC_PROG_AWK
AC_PROG_CC
AC_PROG_CPP

if test "x${COMPILE_JNI}" = xyes; then
  AC_HEADER_STDC
  AC_CHECK_HEADERS(unistd.h sys/types.h sys/config.h sys/ioctl.h asm/ioctls.h inttypes.h stdint.h utime.h sys/utime.h)
  AC_EGREP_HEADER(uint32_t, stdint.h, AC_DEFINE(HAVE_INT32_DEFINED, 1, [Define to 1 if you have uint32_t]))
  AC_EGREP_HEADER(uint32_t, inttypes.h, AC_DEFINE(HAVE_INT32_DEFINED, 1, [Define to 1 if you have uint32_t]))
  AC_EGREP_HEADER(u_int32_t, sys/types.h, AC_DEFINE(HAVE_BSD_INT32_DEFINED, 1, [Define to 1 if you have BSD u_int32_t]))
  AC_EGREP_HEADER(u_int32_t, sys/config.h, AC_DEFINE(HAVE_BSD_INT32_DEFINED, 1, [Define to 1 if you have BSD u_int32_t]))

  AC_CHECK_FUNCS(ftruncate fsync select)
  AC_CHECK_FUNCS(gethostname socket strerror fork pipe execve open close lseek \
	fstat read write htonl memset htons connect getsockname sizeof getpeername \
  	bind listen accept recvfrom send sendto setsockopt getsockopt time mktime)

  AC_HEADER_TIME
  AC_STRUCT_TM
  AC_STRUCT_TIMEZONE

  AC_C_CONST

  dnl FIXME - does not allow cross compiling
  AC_C_BIGENDIAN

  dnl We want to avoid -O2 at the moment, gcc 2.96 from Red Hat 7
  dnl cannot compile some native functions (gcc errors) with this
  dnl flag.
  CFLAGS="-g" 

  dnl We want warnings, lots of warnings :-)
  if test "x${GCC}" = xyes; then
    JAPHAR_GREP_CFLAGS(-Wall, [ CFLAGS="$CFLAGS -Wall" ])
  fi

  dnl Check for AWT related gthread/gtk/libart_lgpl
  if test "x${COMPILE_GTK_PEER}" = xyes; then
     PKG_CHECK_MODULES(GTK, gtk+-2.0 >= 2.2 gthread >= 2.2 libart-2.0)
     AC_SUBST(GTK_CFLAGS)
     AC_SUBST(GTK_LIBS)
  fi

fi

CLASSPATH_WITH_JAVAH

CLASSPATH_WITH_INCLUDEDIR

dnl ----------------------------------------------------------- 
dnl add the include files for the native abstraction layer
dnl -----------------------------------------------------------

EXTRA_INCLUDES="${EXTRA_INCLUDES} -I\$(top_srcdir)/native/target/Linux -I\$(top_srcdir)/native/target/generic"

dnl -----------------------------------------------------------

if test "x${COMPILE_JNI}" = xyes; then
  AC_MSG_CHECKING("jni_md.h support")
  if test -f ${srcdir}/include/jni_md-${target_cpu}-${target_os}.h; then
    AC_MSG_RESULT("yes")
  else
    target_cpu=x86
    target_os=linux-gnu 
    AC_MSG_WARN("no, using x86-linux-gnu")
  fi
  ac_sources="include/jni_md-${target_cpu}-${target_os}.h"
ac_dests="include/jni_md.h"
while test -n "$ac_sources"; do
  set $ac_dests; ac_dest=$1; shift; ac_dests=$*
  set $ac_sources; ac_source=$1; shift; ac_sources=$*
  ac_config_links_1="$ac_config_links_1 $ac_dest:$ac_source"
done
AC_CONFIG_LINKS([$ac_config_links_1])
fi

CLASSPATH_FIND_JAVA
CLASSPATH_FIND_JAVAC

CLASSPATH_WITH_CLASSLIB


dnl -----------------------------------------------------------
dnl Initialize maintainer mode
dnl -----------------------------------------------------------
AM_MAINTAINER_MODE

dnl -----------------------------------------------------------
dnl Enable debugging statements at compile time.  By default
dnl these statements should be optimized out of the bytecode
dnl produced by an optimizing Java compiler and not hinder 
dnl performance because debugging is turned off by default.
dnl -----------------------------------------------------------
AC_ARG_ENABLE([debug],
              [AS_HELP_STRING(--enable-debug,enable runtime debugging code)],
              [case "${enableval}" in 
                yes) 
              	LIBDEBUG="true"
                AC_DEFINE(DEBUG, 1, [Define to 1 if you want native library runtime debugging code enabled]) 
              	;;
                no)   LIBDEBUG="false" ;;
                *) 	AC_MSG_ERROR(bad value ${enableval} for --enable-debug) ;;
              esac],
              [LIBDEBUG="false"])
AC_SUBST(LIBDEBUG)
dnl -----------------------------------------------------------


dnl -----------------------------------------------------------
dnl Enable execution of all static initializer loadLibrary() 
dnl calls at compile time.  By default most people will not
dnl want to disable this, but some VMs (gcj) don't need or want 
dnl them.
dnl -----------------------------------------------------------
AC_ARG_ENABLE([load-library],
              [AS_HELP_STRING(--enable-load-library,enable to use JNI native methods [default=yes]
                                        (disabled automatically using --enable-cni))],
              [case "${enableval}" in 
                yes) INIT_LOAD_LIBRARY="true" ;;
                no) INIT_LOAD_LIBRARY="false" ;;
                *) AC_MSG_ERROR(bad value ${enableval} for --enable-load-library) ;;
              esac],
              [
                if test "x${COMPILE_CNI}" = xyes; then
                  INIT_LOAD_LIBRARY="false"
                else
                  INIT_LOAD_LIBRARY="true"
                fi
              ])
AC_SUBST(INIT_LOAD_LIBRARY)
dnl -----------------------------------------------------------


dnl -----------------------------------------------------------
dnl Small loop to add other things to EXTRA_INCLUDES from a VM
dnl -----------------------------------------------------------
dnl for i in $JAPHAR_CFLAGS $KAFFE_CFLAGS ; do
dnl   add_to_extra_includes=1
dnl   for j in $EXTRA_INCLUDES ; do
dnl     if test $i = $j ; then
dnl       add_to_extra_includes=0
dnl     fi
dnl   done
dnl   if test $add_to_extra_includes -eq 1; then
dnl     EXTRA_INCLUDES="$EXTRA_INCLUDES $i"
dnl   fi
dnl done
dnl -----------------------------------------------------------


dnl Create a link to the VM specific files we're compiling with
if test "x${JVM_REFERENCE}" = x; then
  JVM_REFERENCE=reference
fi
ac_sources="vm/$JVM_REFERENCE"
ac_dests="vm/current"
while test -n "$ac_sources"; do
  set $ac_dests; ac_dest=$1; shift; ac_dests=$*
  set $ac_sources; ac_source=$1; shift; ac_sources=$*
  ac_config_links_2="$ac_config_links_2 $ac_dest:$ac_source"
done
AC_CONFIG_LINKS([$ac_config_links_2])

dnl avoiding automake complaints
REMOVE=""
AC_SUBST(REMOVE)

dnl This is probably useless.
AC_PATH_PROG(SH, sh)
AC_PATH_PROG(MKDIR, mkdir)
AC_PATH_PROG(CP, cp)
AC_PATH_PROG(DATE, date)

dnl According to the GNU coding guide, we shouldn't require find, 
dnl and zip, however GNU provides both so it should be okay 
AC_PATH_PROG(FIND, find)

dnl Useful for disabling zip even if you have it
CLASSPATH_WITH_ZIP

CLASSPATH_ENABLE_GJDOC

dnl -----------------------------------------------------------
dnl -----------------------------------------------------------
AC_ARG_ENABLE([portable-native-sync],
              [AS_HELP_STRING(--enable-portable-native-sync,synchronize VM threads portably)],
              [case "${enableval}" in 
                yes) 
                     AC_DEFINE(PORTABLE_NATIVE_SYNC, 1, [Define to 1 if you want to synchronize VM threads portably])
                     ;;
                no)  ;;
                *)   
                     AC_MSG_ERROR(bad value ${enableval} for --enable-portable-native-sync) 
                     ;;
              esac],
              [])
dnl -----------------------------------------------------------

AC_CONFIG_FILES([Makefile
doc/Makefile
doc/api/Makefile
external/Makefile
gnu/Makefile
gnu/classpath/Makefile
gnu/classpath/Configuration.java
gnu/java/Makefile
gnu/java/awt/Makefile
gnu/java/awt/image/Makefile
gnu/java/awt/peer/Makefile
gnu/java/awt/peer/gtk/Makefile
gnu/java/beans/Makefile
gnu/java/beans/editors/Makefile
gnu/java/beans/info/Makefile
gnu/java/io/Makefile
gnu/java/io/decode/Makefile
gnu/java/io/encode/Makefile
gnu/java/lang/Makefile
gnu/java/lang/reflect/Makefile
gnu/java/locale/Makefile
gnu/java/net/Makefile
gnu/java/net/content/Makefile
gnu/java/nio/Makefile
gnu/java/nio/charset/Makefile
gnu/java/net/content/text/Makefile
gnu/java/net/protocol/Makefile
gnu/java/net/protocol/file/Makefile
gnu/java/net/protocol/http/Makefile
gnu/java/net/protocol/jar/Makefile
gnu/java/math/Makefile
gnu/java/rmi/Makefile
gnu/java/rmi/dgc/Makefile
gnu/java/rmi/registry/Makefile
gnu/java/rmi/rmic/Makefile
gnu/java/rmi/server/Makefile
gnu/java/security/Makefile
gnu/java/security/der/Makefile
gnu/java/security/provider/Makefile
gnu/java/security/util/Makefile
gnu/java/security/x509/Makefile
gnu/java/text/Makefile
gnu/java/util/Makefile
gnu/java/util/prefs/Makefile
gnu/javax/Makefile
gnu/javax/rmi/Makefile
gnu/javax/rmi/CORBA/Makefile
include/Makefile
include/jni.h
java/Makefile
java/applet/Makefile
java/awt/Makefile
java/awt/color/Makefile
java/awt/datatransfer/Makefile
java/awt/dnd/Makefile
java/awt/dnd/peer/Makefile
java/awt/event/Makefile
java/awt/font/Makefile
java/awt/geom/Makefile
java/awt/im/Makefile
java/awt/im/spi/Makefile
java/awt/image/Makefile
java/awt/image/renderable/Makefile
java/awt/peer/Makefile
java/awt/print/Makefile
java/beans/Makefile
java/beans/beancontext/Makefile
java/io/Makefile
java/lang/Makefile
java/lang/ref/Makefile
java/lang/reflect/Makefile
java/math/Makefile
java/net/Makefile
java/nio/Makefile
java/nio/channels/Makefile
java/nio/channels/spi/Makefile
java/nio/charset/Makefile
java/nio/charset/spi/Makefile
java/rmi/Makefile
java/rmi/activation/Makefile
java/rmi/dgc/Makefile
java/rmi/registry/Makefile
java/rmi/server/Makefile
java/security/Makefile
java/security/acl/Makefile
java/security/cert/Makefile
java/security/interfaces/Makefile
java/security/spec/Makefile
java/sql/Makefile
java/text/Makefile
java/util/Makefile
java/util/logging/Makefile
java/util/jar/Makefile
java/util/prefs/Makefile
java/util/regex/Makefile
java/util/zip/Makefile
javax/Makefile
javax/accessibility/Makefile
javax/naming/Makefile
javax/naming/directory/Makefile
javax/naming/event/Makefile
javax/naming/ldap/Makefile
javax/naming/spi/Makefile
javax/print/Makefile
javax/print/attribute/Makefile
javax/rmi/Makefile
javax/rmi/CORBA/Makefile
javax/security/Makefile
javax/security/auth/Makefile
javax/security/auth/x500/Makefile
javax/sql/Makefile
javax/swing/Makefile
javax/swing/border/Makefile
javax/swing/colorchooser/Makefile
javax/swing/event/Makefile
javax/swing/filechooser/Makefile
javax/swing/plaf/Makefile
javax/swing/plaf/basic/Makefile
javax/swing/plaf/metal/Makefile
javax/swing/table/Makefile
javax/swing/text/Makefile
javax/swing/text/html/Makefile
javax/swing/text/html/parser/Makefile
javax/swing/tree/Makefile
javax/swing/undo/Makefile
javax/transaction/Makefile
javax/transaction/xa/Makefile
native/Makefile
native/fdlibm/Makefile
native/cni/Makefile
native/jni/Makefile
native/jni/classpath/Makefile
native/jni/java-io/Makefile
native/jni/java-lang/Makefile
native/jni/java-net/Makefile
native/jni/java-nio/Makefile
native/jni/java-util/Makefile
native/jni/gtk-peer/Makefile
native/target/Makefile
native/target/Linux/Makefile
native/target/generic/Makefile
resource/Makefile
resource/gnu/Makefile
resource/gnu/java/Makefile
resource/gnu/java/awt/Makefile
resource/gnu/java/awt/peer/Makefile
resource/gnu/java/awt/peer/gtk/Makefile
resource/java/Makefile
resource/java/util/Makefile
resource/java/security/Makefile
vm/Makefile
vm/reference/Makefile
vm/reference/java/Makefile
vm/reference/java/lang/Makefile
vm/reference/java/lang/reflect/Makefile
vm/reference/java/io/Makefile
lib/Makefile
lib/gen-classlist.sh])
AC_OUTPUT_COMMANDS([chmod 755 lib/gen-classlist.sh])
AC_OUTPUT
