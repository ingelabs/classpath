dnl Used by aclocal to generate configure

dnl -----------------------------------------------------------
dnl CLASSPATH_CHECK_KAFFE
dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_CHECK_KAFFE],
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

dnl -----------------------------------------------------------
dnl CLASSPATH_WITH_KAFFE - checks for which java virtual machine to use
dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_WITH_KAFFE],
[
  AC_ARG_WITH([kaffe], 
	      [AS_HELP_STRING(--with-kaffe,configure GNU Classpath for Kaffe [default=no])],
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

dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_FIND_JAVAC],
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

dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_WITH_GCJ],
[
  AC_ARG_WITH([gcj],
	      [AS_HELP_STRING(--with-gcj,bytecode compilation with gcj)],
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

dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_CHECK_GCJ],
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
      if expr "$GCJ_VERSION_MINOR" \< 3; then
        GCJ=""
      fi
    fi
    if test "x$GCJ" != x; then
      AC_MSG_RESULT($GCJ_VERSION)
    else
      AC_MSG_WARN($GCJ_VERSION: gcj 3.3 or higher required)
    fi
  fi 
])

dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_WITH_JIKES],
[
  AC_ARG_WITH([jikes],
	      [AS_HELP_STRING(--with-jikes,bytecode compilation with jikes)],
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

dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_CHECK_JIKES],
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

dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_WITH_KJC],
[
  AC_ARG_WITH([kjc], 
  	      [AS_HELP_STRING(--with-kjc=<ksusu.jar>,bytecode compilation with kjc [default=no])],
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

dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_WITH_JAVA],
[
  AC_ARG_WITH([java],
	      [AS_HELP_STRING(--with-java,specify path or name of a java-like program)],
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

dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_CHECK_JAVA],
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

dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_FIND_JAVA],
[
  dnl Place additional bytecode interpreter checks here

  CLASSPATH_WITH_JAVA
])

dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_WITH_JAVAH],
[
  AC_ARG_WITH([javah],
	      [AS_HELP_STRING(--with-javah,specify path or name of a javah-like program)],
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

dnl -----------------------------------------------------------
dnl Checking for a javah like program 
dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_CHECK_JAVAH],
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

dnl -----------------------------------------------------------
dnl CLASSPATH_WITH_CLASSLIB - checks for user specified classpath additions
dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_WITH_CLASSLIB],
[
  AC_ARG_WITH([classpath],
	      [AS_HELP_STRING(--with-classpath,specify path to a classes.zip like file)],
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

dnl -----------------------------------------------------------
dnl CLASSPATH_WITH_INCLUDEDIR - checks for user specified extra include directories
dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_WITH_INCLUDEDIR],
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

dnl -----------------------------------------------------------
dnl CLASSPATH_WITH_GLIBJ - specify what to install
dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_WITH_GLIBJ],
[
  AC_ARG_WITH([glibj],
              [AS_HELP_STRING([--with-glibj],[define what to install (zip|flat|both) [default=zip]])],
              [
                if test "x${withval}" = xyes || test "x${withval}" = xzip; then
      		  AC_PATH_PROG(ZIP, zip)
		  install_class_files=no
		elif test "x${withval}" = xboth; then
		  AC_PATH_PROG(ZIP, zip)
		  install_class_files=yes
		elif test "x${withval}" = xflat; then
		  ZIP=
		  install_class_files=yes
                elif test "x${withval}" = xno || test "x${withval}" = xnone; then
                  ZIP=
		  install_class_files=no
                else
		  AC_MSG_ERROR([unknown value given to --with-glibj])
                fi
	      ],
  	      [
		AC_PATH_PROG(ZIP, zip)
		install_class_files=no
	      ])
  if test "x${ZIP}" = x && test "x${install_class_files}" = xno; then
    AC_MSG_ERROR([you need to either install class files or glibj.zip])
  fi
  AM_CONDITIONAL(INSTALL_GLIBJ_ZIP, test "x${ZIP}" != x)
  AM_CONDITIONAL(INSTALL_CLASS_FILES, test "x${install_class_files}" = xyes)
])

dnl -----------------------------------------------------------
dnl Enable generation of API documentation, assumes gjdoc
dnl has been compiled to an executable or a suitable script
dnl is in your PATH
dnl -----------------------------------------------------------
AC_DEFUN([CLASSPATH_ENABLE_GJDOC],
[
  AC_ARG_ENABLE([gjdoc],
                [AS_HELP_STRING([--enable-gjdoc],[enable API doc. generation [default=no]])],
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

