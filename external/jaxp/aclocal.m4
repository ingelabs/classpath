
AC_DEFUN([IFVAL],
[ifelse([$1], [], [$3], [$2])])


# AC_PROG_JAVA_CC([COMPILER ...])
# --------------------------
# COMPILER ... is a space separated list of java compilers to search for.
# This just gives the user an opportunity to specify an alternative
# search list for the java compiler.
# The compiler is set in the variable JAVA_CC and the compiler options
# are set in the variable JAVA_CC_OPTS
AC_DEFUN([AC_PROG_JAVA_CC],
[IFVAL([$1],
      [AC_CHECK_PROGS(JAVA_CC, [$1], , $PATH)],
[AC_CHECK_PROG(JAVA_CC, gcj, gcj, , $PATH)
if test -z "$JAVA_CC"; then
  AC_CHECK_PROG(JAVA_CC, javac, javac, , $PATH)
fi
if test -z "$JAVA_CC"; then
  AC_CHECK_PROG(JAVA_CC, jikes, jikes, , $PATH)
fi
])

if test "$JAVA_CC" = "gcj"; then
   if test "$GCJ_OPTS" = ""; then
      AC_SUBST(GCJ_OPTS,-C)
      echo > /dev/null
   fi
   AC_SUBST(JAVA_CC_OPTS, @GCJ_OPTS@,
	[Define the compilation options for GCJ])
fi
test -z "$JAVA_CC" && AC_MSG_ERROR([no acceptable java compiler found in \$PATH])
])# AC_PROG_JAVA_CC
