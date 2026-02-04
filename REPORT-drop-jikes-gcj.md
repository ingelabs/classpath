# Report: Drop jikes and gcj compiler support

## Summary

Removed jikes and gcj as supported Java compilers from the GNU Classpath build system. Both are dead projects. This simplifies the three-way `if GCJ_JAVAC / else if ECJ_JAVAC / else` blocks in Makefiles down to two-way `if ECJ_JAVAC / else`. The gcjwebplugin (`native/plugin/`) was left untouched. Documentation files (NEWS, README, texinfo) were also left untouched as they contain historical references.

## Files modified

### 1. `m4/ac_prog_javac.m4`
- Removed `GCJ_OPTS="-g"` variable
- Removed `"gcj$EXEEXT -C"` from both `AC_CHECK_PROGS` search lists
- Removed the `AC_CACHE_CHECK` block that detected whether `$JAVAC` is gcj
- Removed `AC_SUBST(JAVAC_IS_GCJ, ...)`
- Removed `AM_CONDITIONAL(GCJ_JAVAC, ...)`

### 2. `m4/ac_prog_javac_works.m4`
- Removed the `if test x$JAVAC_IS_GCJ = xyes` branch (which used `-fsource`/`-ftarget` gcj flags)
- The compiler test now unconditionally uses the standard `-sourcepath '' -source 1.6 -target 1.6` flags

### 3. `m4/acinclude.m4`
- Removed `gcjh-wrapper-4.1` and `gcjh-4.1` from the `AC_PATH_PROGS` search list for `USER_JAVAH`
- Removed the `if test x$JAVAC_IS_GCJ != xyes` guard around the `-J` memory option check (the check itself handles failure gracefully, so it now runs unconditionally)

### 4. `configure.ac`
- Updated comment to no longer mention `GCJ_JAVAC`
- Removed `AM_CONDITIONAL(GCJ_JAVAC, false)` from the else branch (where the compiler isn't needed)

### 5. `lib/Makefile.am`
- Removed `GCJ_OPTS` variable definition
- Collapsed `if GCJ_JAVAC / else if ECJ_JAVAC / else / endif / endif` into `if ECJ_JAVAC / else / endif`
- Updated the comment from "handling source to bytecode compiler programs like gcj, jikes and kjc" to "handling source to bytecode compiler programs"
- Removed the commented-out `#if FOUND_GCJ` block (lines 135-150) that contained a dead recursive-make rule for gcj
- Removed `Makefile.gcj` and `split-for-gcj.sh` from `EXTRA_DIST`

### 6. `tools/Makefile.am`
- Removed `GCJ_OPTS` variable definition
- Collapsed `if GCJ_JAVAC / else if ECJ_JAVAC / else / endif / endif` into `if ECJ_JAVAC / else / endif`

### 7. `examples/Makefile.am`
- Removed `GCJ_OPTS` variable definition
- Collapsed `if GCJ_JAVAC / else if ECJ_JAVAC / else / endif / endif` into `if ECJ_JAVAC / else / endif`

### 8. `doc/Makefile.am`
- Removed `gcjh.1` from the `TOOLS_MANFILES` man page list
- Removed `gcjh.pod` from the `.INTERMEDIATE` list
- Removed the `gcjh.pod` build rule (which generated the pod from `cp-tools.texinfo`)

## Files deleted

- `lib/Makefile.gcj` -- GCJ-specific recursive Makefile
- `lib/split-for-gcj.sh` -- helper script for GCJ compilation

## Verification

- Grepped all modified `.m4` and `.am` files and `configure.ac` for `GCJ_JAVAC`, `JAVAC_IS_GCJ`, `GCJ_OPTS`, `gcjh`, `Makefile.gcj`, and `split-for-gcj` -- no leftover references found.
