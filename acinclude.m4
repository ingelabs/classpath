dnl Used by aclocal to generate configure

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

dnl CLASSPATH_CHECK_JAPHAR
AC_DEFUN(CLASSPATH_CHECK_JAPHAR,
[
  if test "x$1" = x; then
    AC_PATH_PROG(JAPHAR_CONFIG, japhar-config, "", $PATH:/usr/local/japhar/bin:/usr/japhar/bin)
  else
    AC_PATH_PROG(JAPHAR_CONFIG, japhar-config, "", $1/bin:$PATH)
  fi
  if test "x${JAPHAR_CONFIG}" = x; then
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
  fi
  if test $JAPHAR_CLASSLIB ; then
    AC_MSG_RESULT(yes)
  else
    AC_MSG_RESULT(no)
  fi
  AC_SUBST(JAPHAR_CLASSLIB)
])

dnl CLASSPATH_CHECK_KAFFE
AC_DEFUN(CLASSPATH_CHECK_KAFFE,
[
  AC_PATH_PROG(KAFFE_CONFIG, kaffe-config, "", $PATH:/usr/local/kaffe/bin:/usr/kaffe/bin)
  if test "x${KAFFE_CONFIG}" = x; then
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

  conditional_with_kaffe=true
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

  AC_MSG_CHECKING(for kjc)
  if test -e $_t_datadir/kaffe/kjc.jar; then
    KJC_CLASSPATH=$_t_datadir/kaffe/kjc.jar
    AC_SUBST(KJC_CLASSPATH)
    conditional_with_kjc=true
    AC_MSG_RESULT(${withval})
  elif test -e $_t_datadir/kjc.jar; then
    KJC_CLASSPATH=$_t_datadir/kjc.jar
    AC_SUBST(KJC_CLASSPATH)
    conditional_with_kjc=true
    AC_MSG_RESULT(${withval})
  else
    conditional_with_kjc=false
    AC_MSG_RESULT(no)
  fi
  
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
    AC_MSG_RESULT(no)
  fi
  AC_MSG_RESULT(yes)
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

dnl CLASSPATH_WITH_JAPHAR - checks for japhar
AC_DEFUN(CLASSPATH_WITH_JAPHAR,
[
  AC_ARG_WITH(japhar, 
  [  --with-japhar		  configure GNU Classpath for Japhar [default=yes]],
  [
    if test "x${withval}" = xyes || test "x${withval}" = x; then
      CLASSPATH_CHECK_JAPHAR
    elif test "x${withval}" != xno || test "x${withval}" != xfalse; then
      CLASSPATH_CHECK_JAPHAR(${withval})
    fi
  ],
  [ 
    conditional_with_japhar=false
    JAPHAR_CFLAGS=""
    AC_SUBST(JAPHAR_CFLAGS)
  ])
])

dnl CLASSPATH_WITH_KAFFE - checks for which java virtual machine to use
AC_DEFUN(CLASSPATH_WITH_KAFFE,
[
  AC_ARG_WITH(kaffe, 
  [  --with-kaffe		  configure GNU Classpath for Kaffe [default=no]],
  [   
    if test "x${withval}" = xyes || test "x${withval}" = x; then
      CLASSPATH_CHECK_KAFFE
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


AC_DEFUN(CLASSPATH_FIND_JAVAC,
[
  user_specified_javac=

  CLASSPATH_WITH_GCJ
  CLASSPATH_WITH_JIKES
  CLASSPATH_WITH_KJC

  if test "x${user_specified_javac}" = x; then
    AM_CONDITIONAL(FOUND_GCJ, test "x${GCJ}" != x)
    AM_CONDITIONAL(FOUND_JIKES, test "x${JIKES}" != x)
  else
    AM_CONDITIONAL(FOUND_GCJ, test "x${user_specified_javac}" = xgcj)
    AM_CONDITIONAL(FOUND_JIKES, test "x${user_specified_javac}" = xjikes)
  fi
  AM_CONDITIONAL(FOUND_KJC, test "x${user_specified_javac}" = xkjc)

  if test "x${GCJ}" = x && test "x${JIKES}" = x && test "x${user_specified_javac}" != xkjc; then
      echo "configure: cannot find javac, try --with-gcj, --with-jikes, or --with-kjc" 1>&2
      exit 1    
  fi
])


AC_DEFUN(CLASSPATH_WITH_GCJ,
[
  AC_ARG_WITH(gcj,
  [  --with-gcj              bytecode compilation with gcj ],
  [
    if test "x${withval}" != x && test "x${withval}" != xyes && test "x${withval}" != xno; then
      CLASSPATH_CHECK_GCJ(${withval})
    else
      if test "x${withval}" != xno; then
        CLASSPATH_CHECK_GCJ
      fi
    fi
    user_specified_javac=gcj
  ],
  [
    CLASSPATH_CHECK_GCJ
  ])
  AM_CONDITIONAL(USER_SPECIFIED_GCJ, test "x${GCJ}" != x)
  AC_SUBST(GCJ)
])


AC_DEFUN(CLASSPATH_CHECK_GCJ,
[
  if test "x$1" != x; then
    if test -f "$1"; then
      GCJ="$1"
    else
      AC_PATH_PROG(GCJ, "$1")
    fi
  else
    AC_PATH_PROG(GCJ, "gcj")
  fi  

  if test "x$GCJ" != x; then
    AC_MSG_CHECKING(gcj version)
    GCJ_VERSION=`$GCJ --version`
    GCJ_VERSION_MAJOR=`echo "$GCJ_VERSION" | cut -d '.' -f 1`
    GCJ_VERSION_MINOR=`echo "$GCJ_VERSION" | cut -d '.' -f 2`

    if expr "$GCJ_VERSION_MAJOR" \< 3 > /dev/null; then
      GCJ=""
    fi
    if expr "$GCJ_VERSION_MAJOR" = 3 > /dev/null; then
      if expr "$GCJ_VERSION_MINOR" \< 1; then
        GCJ=""
      fi
    fi
    if test "x$GCJ" != x; then
      AC_MSG_RESULT($GCJ_VERSION)
    else
      AC_MSG_WARN($GCJ_VERSION: gcj 3.1 or higher required)
    fi
  fi 
])


AC_DEFUN(CLASSPATH_WITH_JIKES,
[
  AC_ARG_WITH(jikes,
  [  --with-jikes		  bytecode compilation with jikes ],
  [
    if test "x${withval}" != x && test "x${withval}" != xyes && test "x${withval}" != xno; then
      CLASSPATH_CHECK_JIKES(${withval})
    else
      if test "x${withval}" != xno; then
        CLASSPATH_CHECK_JIKES
      fi
    fi
    user_specified_javac=jikes
  ],
  [ 
    CLASSPATH_CHECK_JIKES
  ])
  AM_CONDITIONAL(USER_SPECIFIED_JIKES, test "x${JIKES}" != x)
  AC_SUBST(JIKES)
])


AC_DEFUN(CLASSPATH_CHECK_JIKES,
[
  if test "x$1" != x; then
    if test -f "$1"; then
      JIKES="$1"
    else
      AC_PATH_PROG(JIKES, "$1")
    fi
  else
    AC_PATH_PROG(JIKES, "jikes")
  fi
])


AC_DEFUN(CLASSPATH_WITH_KJC,
[
  AC_ARG_WITH(kjc, 
  [  --with-kjc=<ksusu.jar>  bytecode compilation with kjc [default=no]],
  [
    if test "x${withval}" != xno; then
      AC_MSG_CHECKING(for kjc)
      if test "x${withval}" = x || test "x${withval}" = xyes; then
        AC_MSG_ERROR(specify the location of ksusu.jar or kjc CLASSPATH)
      fi
      KJC_CLASSPATH=${withval}
      AC_SUBST(KJC_CLASSPATH)
      conditional_with_kjc=true
      AC_MSG_RESULT(${withval})
    fi
    user_specified_javac=kjc
  ],
  [ 
    conditional_with_kjc=false
  ])


  AM_CONDITIONAL(USER_SPECIFIED_KJC, test "x${conditional_with_kjc}" = xtrue)
  if test "x${conditional_with_kjc}" = xtrue && test "x${USER_JABBA}" = x; then
    if test "x${USER_JABBA}" = x; then
      echo "configure: cannot find java, try --with-java" 1>&2
      exit 1
    fi
  fi
])


AC_DEFUN(CLASSPATH_WITH_JAVA,
[
  AC_ARG_WITH(java,
  [  --with-java		  specify path or name of a java-like program ],
  [
    if test "x${withval}" != x && test "x${withval}" != xyes && test "x${withval}" != xno; then
      CLASSPATH_CHECK_JAVA(${withval})
    else
      if test "x${withval}" != xno; then
        CLASSPATH_CHECK_JAVA
      fi
    fi
  ],
  [ 
    CLASSPATH_CHECK_JAVA
  ])
  AM_CONDITIONAL(USER_SPECIFIED_JABBA, test "x${USER_JABBA}" != x)
  AC_SUBST(USER_JABBA)
])


AC_DEFUN(CLASSPATH_CHECK_JAVA,
[
  if test "x$1" != x; then
    if test -f "$1"; then
      USER_JABBA="$1"
    else
      AC_PATH_PROG(USER_JABBA, "$1")
    fi
  else
    AC_PATH_PROG(USER_JABBA, "java")
  fi
])


AC_DEFUN(CLASSPATH_FIND_JAVA,
[
  dnl Place additional bytecode interpreter checks here

  CLASSPATH_WITH_JAVA
])


AC_DEFUN(CLASSPATH_WITH_JAVAH,
[
  AC_ARG_WITH(javah,
  [  --with-javah		  specify path or name of a javah-like program ],
  [
    if test "x${withval}" != x && test "x${withval}" != xyes && test "x${withval}" != xno; then
      CLASSPATH_CHECK_JAVAH(${withval})
    else
      CLASSPATH_CHECK_JAVAH
    fi
  ],
  [ 
    CLASSPATH_CHECK_JAVAH
  ])
  AM_CONDITIONAL(USER_SPECIFIED_JAVAH, test "x${USER_JAVAH}" != x)
  AC_SUBST(USER_JAVAH)
])

dnl Checking for a javah like program 
AC_DEFUN(CLASSPATH_CHECK_JAVAH,
[
  if test "x$1" != x; then
    if test -f "$1"; then
      USER_JAVAH="$1"
    else
      AC_PATH_PROG(USER_JAVAH, "$1")
    fi
  else
    for javah_name in gcjh javah; do
      AC_PATH_PROG(USER_JAVAH, "$javah_name")
      if test "x${USER_JAVAH}" != x; then
        break
      fi
    done
  fi
  
#  if test "x${USER_JAVAH}" = x; then
#    echo "configure: cannot find javah" 1>&2
#    exit 1
#  fi
])

dnl CLASSPATH_WITH_CLASSLIB - checks for user specified classpath additions
AC_DEFUN(CLASSPATH_WITH_CLASSLIB,
[
  AC_ARG_WITH(classpath,
  [  --with-classpath        specify path to a classes.zip like file ],
  [
    if test "x${withval}" = xyes; then
      # set user classpath to CLASSPATH from env
      AC_MSG_CHECKING(for classlib)
      USER_CLASSLIB=${CLASSPATH}
      AC_SUBST(USER_CLASSLIB)
      AC_MSG_RESULT(${USER_CLASSLIB})
      conditional_with_classlib=true      
    elif test "x${withval}" != x && test "x${withval}" != xno; then
      # set user classpath to specified value
      AC_MSG_CHECKING(for classlib)
      USER_CLASSLIB=${withval}
      AC_SUBST(USER_CLASSLIB)
      AC_MSG_RESULT(${withval})
      conditional_with_classlib=true
    fi
  ],
  [ conditional_with_classlib=false ])
  AM_CONDITIONAL(USER_SPECIFIED_CLASSLIB, test "x${conditional_with_classlib}" = xtrue)
])


dnl CLASSPATH_WITH_INCLUDEDIR - checks for user specified extra include directories
AC_DEFUN(CLASSPATH_WITH_INCLUDEDIR,
[
  AC_ARG_WITH(includedir,
  [  --with-includedir=DIR   specify path to an extra include dir ],
  [
    AC_MSG_CHECKING(for includedir)
    if test "x${withval}" != x && test "x${withval}" != xyes && test "x${withval}" != xno; then
      if test -r ${withval}; then
        if test "x${EXTRA_INCLUDES}" = x; then
          EXTRA_INCLUDES="-I${withval}"
        else
          EXTRA_INCLUDES="${EXTRA_INCLUDES} -I${withval}"
        fi
        AC_SUBST(EXTRA_INCLUDES)
        AC_MSG_RESULT("added ${withval}")
      else
        AC_MSG_RESULT("${withval} does not exist")
      fi
    fi
  ],
  [
    if test -z "$EXTRA_INCLUDES"; then
      EXTRA_INCLUDES=""
      AC_SUBST(EXTRA_INCLUDES)
    fi
  ])
])

dnl CLASSPATH_WITH_ZIP - allow user to specify without zip
AC_DEFUN(CLASSPATH_WITH_ZIP,
[
  AC_ARG_WITH(zip, 
  [  --with-zip		  create glibj.zip [default=yes]],
  [
    if test "x${withval}" = xyes || test "x${withval}" = x; then
      AC_PATH_PROG(ZIP, zip)
    elif test "x${withval}" = xno || test "x${withval}" = xfalse; then
      ZIP=
    else
      ZIP="${withval}"
    fi
  ],
  [ 
    AC_PATH_PROG(ZIP, zip)
  ])
  AC_SUBST(ZIP)
  AM_CONDITIONAL(HAVE_ZIP, test "x${ZIP}" != x)
])

dnl -----------------------------------------------------------
dnl Enable generation of API documentation, assumes gjdoc
dnl has been compiled to an executable or a suitable script
dnl is in your PATH
dnl -----------------------------------------------------------
AC_DEFUN(CLASSPATH_ENABLE_GJDOC,
[
  AC_ARG_ENABLE(gjdoc,
  [  --enable-gjdoc           enable API doc. generation [default=no]],
  [
    case "${enableval}" in
      yes) ENABLE_GJDOC=yes ;;
      no) ENABLE_GJDOC=no ;;
      *) ENABLE_GJDOC=yes ;;
    esac
    if test "x${ENABLE_GJDOC}" = xyes; then
      AC_PATH_PROG(GJDOC, gjdoc)
      AC_PATH_PROG(XMLCATALOG, xmlcatalog)
      AC_PATH_PROG(XSLTPROC, xsltproc)
    fi
  ],
  [ENABLE_GJDOC=no])

  AM_CONDITIONAL(CREATE_API_DOCS, test "x${ENABLE_GJDOC}" = xyes)
])

