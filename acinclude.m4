dnl
dnl Added macros
dnl JAPHAR_GREP_CFLAGS
dnl CLASSPATH_CHECK_JAPHAR
dnl CLASSPATH_CHECK_KAFFE
dnl CLASSPATH_CHECK_THREADS
dnl CLASSPATH_CHECK_GLIB
dnl CLASSPATH_CHECK_GTK
dnl

dnl JAPHAR_GREP_CFLAGS(flag, cmd_if_missing, cmd_if_present)
AC_DEFUN(JAPHAR_GREP_CFLAGS,
[case "$CFLAGS" in
"$1" | "$1 "* | *" $1" | *" $1 "* )
  ifelse($#, 3, [$3], [:])
  ;;
*)
  $2
  ;;
esac
])

dnl CLASSPATH_INTERNAL_CHECK_JAPHAR
AC_DEFUN(CLASSPATH_INTERNAL_CHECK_JAPHAR,
[
  AC_PATH_PROG(JAPHAR_CONFIG, japhar-config, "", $PATH:/usr/local/japhar/bin:/usr/japhar/bin)
  if test "$JAPHAR_CONFIG" = ""; then
    echo "configure: cannot find japhar-config: is Japhar installed?" 1>&2
    exit 1
  fi
  AC_MSG_CHECKING(for Japhar)
  JAPHAR_PREFIX="`$JAPHAR_CONFIG --prefix`"
  JAPHAR_CFLAGS="`$JAPHAR_CONFIG compile`"
  JAPHAR_LIBS="`$JAPHAR_CONFIG link`"
  JVM="yes"
  JVM_REFERENCE="reference"
  AC_SUBST(JAPHAR_PREFIX)
  AC_SUBST(JAPHAR_CFLAGS)
  AC_SUBST(JAPHAR_LIBS)
  AC_SUBST(JVM)
  AC_SUBST(JVM_REFERENCE)
  conditional_with_japhar=true
  AC_MSG_RESULT(yes)

  dnl define WITH_JAPHAR for native compilation
  AC_DEFINE(WITH_JAPHAR)

  dnl Reset prefix so that we install into Japhar directory
  prefix=$JAPHAR_PREFIX
  AC_SUBST(prefix)

  dnl programs we probably need somewhere
  _t_bindir=`$JAPHAR_CONFIG info bindir`
  _t_datadir=`$JAPHAR_CONFIG info datadir`
  AC_PATH_PROG(JAPHAR_JABBA, japhar, "", $_t_bindir:$PATH)
  AC_PATH_PROG(JAPHAR_JAVAC, javac, "", $_t_bindir:$PATH)
  AC_PATH_PROG(JAPHAR_JAVAH, javah, "", $_t_bindir:$PATH)
  AC_MSG_CHECKING(for Japhar classes)
  if test -e $_t_datadir/classes.zip; then
    JAPHAR_CLASSLIB=$_t_datadir/classes.zip
  elif test -e $_t_datadir/classes.jar; then
    JAPHAR_CLASSLIB=$_t_datadir/classes.jar
  elif test -e $_t_datadir/rt.jar; then
    JAPHAR_CLASSLIB=$_t_datadir/rt.jar
  elif test -e $_t_datadir/rt.zip; then
    JAPHAR_CLASSLIB=$_t_datadir/rt.zip
  else
    AC_MSG_ERROR(no)
  fi
  AC_MSG_RESULT(yes)
  AC_SUBST(JAPHAR_CLASSLIB)
])

dnl CLASSPATH_INTERNAL_CHECK_KAFFE
AC_DEFUN(CLASSPATH_INTERNAL_CHECK_KAFFE,
[
  AC_PATH_PROG(KAFFE_CONFIG, kaffe-config, "", $PATH:/usr/local/kaffe/bin:/usr/kaffe/bin)
  if test "$KAFFE_CONFIG" = ""; then
    echo "configure: cannot find kaffe-config: is Kaffe installed?" 1>&2
    exit 1
  fi
  AC_MSG_CHECKING(for Kaffe)

  KAFFE_PREFIX="`$KAFFE_CONFIG --prefix`"
  KAFFE_CFLAGS="`$KAFFE_CONFIG compile`"
  KAFFE_LIBS="`$KAFFE_CONFIG link`"
  JVM="yes"
  JVM_REFERENCE="kaffe"
  AC_SUBST(KAFFE_PREFIX)
  AC_SUBST(KAFFE_CFLAGS)
  AC_SUBST(KAFFE_LIBS)
  AC_SUBST(JVM)
  AC_SUBST(JVM_REFERENCE)

  dnl conditional_with_kaffe
  AC_MSG_RESULT(yes)

  dnl define WITH_KAFFE for native compilation
  AC_DEFINE(WITH_KAFFE)

  dnl Reset prefix so that we install into the Kaffe directory
  prefix=$KAFFE_PREFIX
  AC_SUBST(prefix)

  dnl programs we probably need somewhere
  _t_bindir=`$KAFFE_CONFIG info bindir`
  _t_datadir=`$KAFFE_CONFIG info datadir`
  AC_PATH_PROG(KAFFE_JABBA, kaffe, "", $_t_bindir:$PATH)
  AC_PATH_PROG(KAFFE_JAVAC, kjc, "", $_t_bindir:$PATH)
  AC_PATH_PROG(KAFFE_JAVAH, kaffeh, "", $_t_bindir:$PATH)

  AC_MSG_CHECKING(for Kaffe classes)
  KAFFE_CLASSLIB=""
  if test -e $_t_datadir/glibj.jar; then
    KAFFE_CLASSLIB=$_t_datadir/glibj.jar
  elif test -e $_t_datadir/kaffe/glibj.jar; then
    KAFFE_CLASSLIB=$_t_datadir/kaffe/glibj.jar
  elif test -e $_t_datadir/Klasses.jar; then
    KAFFE_CLASSLIB=$_t_datadir/Klasses.jar
  elif test -e $_t_datadir/kaffe/Klasses.jar; then
    KAFFE_CLASSLIB=$_t_datadir/kaffe/Klasses.jar
  else
    AC_MSG_ERROR(no)
  fi
  AC_MSG_RESULT(yes)
  if test -e $_t_datadir/kjc.jar; then
    KAFFE_CLASSLIB=$KAFFE_CLASSLIB:$_t_datadir/kjc.jar
  fi
  if test -e $_t_datadir/kaffe/kjc.jar; then
    KAFFE_CLASSLIB=$KAFFE_CLASSLIB:$_t_datadir/kaffe/kjc.jar
  fi
  if test -e $_t_datadir/rmi.jar; then
    KAFFE_CLASSLIB=$KAFFE_CLASSLIB:$_t_datadir/rmi.jar
  fi
  if test -e $_t_datadir/kaffe/rmi.jar; then
    KAFFE_CLASSLIB=$KAFFE_CLASSLIB:$_t_datadir/kaffe/rmi.jar
  fi
  if test -e $_t_datadir/tools.jar; then
    KAFFE_CLASSLIB=$KAFFE_CLASSLIB:$_t_datadir/tools.jar
  fi
  if test -e $_t_datadir/kaffe/tools.jar; then
    KAFFE_CLASSLIB=$KAFFE_CLASSLIB:$_t_datadir/kaffe/tools.jar
  fi

  AC_SUBST(KAFFE_CLASSLIB)
])

dnl CLASSPATH_CHECK_JAPHAR - checks for japhar
AC_DEFUN(CLASSPATH_CHECK_JAPHAR,
[
  AC_ARG_WITH(japhar, 
  [  --with-japhar		  configure GNU Classpath for Japhar [default=yes]],
  [
    if test ${withval} = "yes" || test ${withval} = ""; then
      CLASSPATH_INTERNAL_CHECK_JAPHAR
    fi
  ],
  [ 
    conditional_with_japhar=false
    JAPHAR_CFLAGS=""
    AC_SUBST(JAPHAR_CFLAGS)
  ])
])

dnl CLASSPATH_CHECK_KAFFE - checks for which java virtual machine to use
AC_DEFUN(CLASSPATH_CHECK_KAFFE,
[
  AC_ARG_WITH(kaffe, 
  [  --with-kaffe		  configure GNU Classpath for Kaffe [default=no]],
  [   
    if test ${withval} = "yes" || test ${withval} = ""; then
      CLASSPATH_INTERNAL_CHECK_KAFFE
    fi
  ],
  [ conditional_with_kaffe=false
    KAFFE_CFLAGS=""
    AC_SUBST(KAFFE_CFLAGS)
  ])
])

dnl threads packages (mostly stolen from Japhar)
dnl given that japhar-config gives -lpthread, may not need this (cbj)
AC_DEFUN(CLASSPATH_CHECK_THREADS,
[
  threads=no

  if test "x${threads}" = xno; then
    AC_CHECK_LIB(threads, cthread_fork)
    if test "x${ac_cv_lib_threads_cthread_fork}" = xyes; then
        AC_DEFINE(USE_CTHREADS, )
        threads=yes
    fi
  fi

  if test "x${threads}" = xno; then
    AC_CHECK_FUNCS(_beginthreadex)
    if test "x${ac_cv_CreateThread}" = xyes; then
        AC_DEFINE(USE_WIN32_THREADS, )
        AC_CHECK_FUNCS(CloseHandle SetThreadPriority ExitThread Sleep \
                GetCurrentThreadId TlsAlloc TlsSetValue TlsGetValue)
        threads=yes
    fi
  fi

  if test "x${threads}" = xno; then
    AC_CHECK_LIB(pthread, pthread_create)
    if test "x${ac_cv_lib_pthread_pthread_create}" = xyes; then
      threads=yes
    fi

    if test "x${threads}" = xno; then
      AC_CHECK_LIB(c_r, pthread_create)
      if test "x${ac_cv_lib_c_r_pthread_create}" = xyes; then
        threads=yes
      fi
    fi

    if test "x${threads}" = xno; then
      # HP/UX 10.20 uses -lcma
      AC_CHECK_LIB(cma, pthread_create)
      if test "x${ac_cv_lib_cma_pthread_create}" = xyes; then
        threads=yes
      fi
    fi

    if test "x${threads}" = xno; then
      AC_CHECK_LIB(c, pthread_create)
      if test "x${ac_cv_lib_c_pthread_create}" = xyes; then
        threads=yes
      fi
    fi

    if test "x${threads}" = xyes; then
      AC_DEFINE(USE_PTHREADS, )
    fi
  fi
])

dnl CLASSPATH_INTERNAL_CHECK_JIKES
AC_DEFUN(CLASSPATH_INTERNAL_CHECK_JIKES,
[
  AC_PATH_PROG(JIKES, jikes, "", $PATH:/usr/local/bin)
  if test "$JIKES" = ""; then
    echo "configure: cannot find jikes: is jikes in your path?" 1>&2
    exit 1
  fi
])


dnl CLASSPATH_CHECK_JIKES - checks for jikes
AC_DEFUN(CLASSPATH_CHECK_JIKES,
[
  AC_ARG_WITH(jikes, 
  [  --with-jikes		  compile classes with jikes [default=no]],
  [
    if test ${withval} != "no"; then
      if test ${withval} = "" || test ${withval} = "yes"; then
        CLASSPATH_INTERNAL_CHECK_JIKES
      else 
        JIKES=${withval}
	AC_SUBST(JIKES)
      fi
      conditional_with_jikes=true
    fi
  ],
  [ 
    conditional_with_jikes=false
  ])
])


dnl CLASSPATH_CHECK_KJC - checks for kjc
AC_DEFUN(CLASSPATH_CHECK_KJC,
[
  AC_ARG_WITH(kjc, 
  [  --with-kjc=DIR	  compile classes with kjc [default=no]],
  [
    if test ${withval} != "no"; then
      AC_MSG_CHECKING(for kjc)
      if test ${withval} = "" || test ${withval} = "yes"; then
        AC_MSG_ERROR(specify a classpath to kjc)
      fi
      KJC_CLASSPATH=${withval}
      AC_SUBST(KJC_CLASSPATH)
      conditional_with_kjc=true
      AC_MSG_RESULT(${withval})
    fi
  ],
  [ 
    conditional_with_kjc=false
  ])
])

