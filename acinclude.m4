dnl
dnl Add macros
dnl JAPHAR_GREP_CFLAGS
dnl CLASSPATH_CHECK_JAPHAR
dnl CLASSPATH_CHECK_KAFFE
dnl CLASSPATH_CHECK_THREADS
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
  JAPHAR_CFLAGS="`$JAPHAR_CONFIG compile`"
  JAPHAR_LIBS="`$JAPHAR_CONFIG link`"
  JVM="yes"
  AC_SUBST(JAPHAR_CFLAGS)
  AC_SUBST(JAPHAR_LIBS)
  AC_SUBST(JVM)
  AM_CONDITIONAL(JAPHAR, test x = x)
  AC_MSG_RESULT(yes)
])

dnl CLASSPATH_INTERNAL_CHECK_KAFFE
AC_DEFUN(CLASSPATH_INTERNAL_CHECK_KAFFE,
[
  AC_MSG_CHECKING(for Kaffe)
  JVM="no"
  AC_SUBST(JVM)
  AC_MSG_ERROR(Help GNU Classpath support Kaffe!)
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