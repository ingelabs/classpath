dnl
dnl Add macros
dnl JAPHAR_GREP_CFLAGS
dnl CLASPATH_CHECK_JVM
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

dnl CLASSPATH_INTERNAL_CHECK_JVM(jvm_name)
AC_DEFUN(CLASSPATH_INTERNAL_CHECK_JVM,
[
  if test "$1" = "japhar"; then
dnl I get errors trying to use AC_PATH_PROG, so screw it
dnl    AC_PATH_PROG(JAPHARCONFIG, japhar, "", $PATH:/usr/local/japhar/bin:/usr/japhar/bin)
dnl    if test "$JAPHARCONFIG" = ""; then
dnl      AC_MSG_ERROR("configure: cannot find japhar-config: is Japhar installed?")
dnl      exit 1
dnl    fi
    japhar-config --cflags > /dev/null || {
      AC_MSG_ERROR("configure: cannot execute japhar-config: is Japhar installed?")
      echo "configure: cannot execute japhar-config: is Japhar installed?" 1>&2
      exit 1
    }
    JAPHAR_CFLAGS="`japhar-config --cflags`"
    JAPHAR_JNI_LIBS="`japhar-config --jni-libs`"
    AC_SUBST(JAPHAR_CFLAGS)
    AC_SUBST(JAPHAR_JNI_LIBS)
    AM_CONDITIONAL(JAPHAR, true)
    AC_MSG_RESULT("yes")
  elif test "$1" = "kaffe"; then
    AC_MSG_ERROR("configure: please help make classpath support kaffe!")
  else
    AC_MSG_ERROR("configure: unknown jvm $1, try `japhar' or `kaffe' instead")
  fi
  # add other vms to the if clause with elif 
])

dnl CLASSPATH_CHECK_JVM - checks for which java virtual machine to use
AC_DEFUN(CLASSPATH_CHECK_JVM,
[
  AC_ARG_WITH(jvm, 
  [  --with-jvm		  specify java virtual machine: default=japhar],
  [   
    if test ${withval} = ""; then
      # japhar is default
      AC_MSG_CHECKING(for Japhar)
      CLASSPATH_INTERNAL_CHECK_JVM("japhar")
    elif test ${withval} = "yes" || test ${withval} = "no"; then
      # argument unspecified
      AC_MSG_CHECKING(for Japhar)
      CLASSPATH_INTERNAL_CHECK_JVM("japhar")
      if test -z "$JAPHAR_CFLAGS"; then
        AC_MSG_CHECKING(for Kaffe)
        CLASSPATH_INTERNAL_CHECK_JVM("kaffe")
      fi
    else
      # argument specified
      AC_MSG_CHECKING(for ${withval})
      CLASSPATH_INTERNAL_CHECK_JVM(${withval})
    fi
  ], 
  [
    # argument unspecified
    AC_MSG_CHECKING(for Japhar)
    CLASSPATH_INTERNAL_CHECK_JVM("japhar")
    if test -z "$JAPHAR_CFLAGS"; then
      AC_MSG_CHECKING(for Kaffe)
      CLASSPATH_INTERNAL_CHECK_JVM("kaffe")
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