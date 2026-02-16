# Review: Phvega Calendar changes (commit2, on top of VMR)

## Overview

The Phvega patch is a fixup on top of the VMR implementation.  It
addresses some of the issues found in the VMR patch, introduces named
constants and an overflow-protection mechanism, but also introduces new
concerns.

This document lists the issues found in the patch, along with
verification results from testing against OpenJDK, VMR, and our fix.

## Issues fixed from VMR

### 1. `clone()` now copies `fieldsPriority` — VERIFIED FIX

The `clone()` method is updated to deep-copy the array:

```java
cal.fieldsPriority = fieldsPriority.clone();
```

This fixes the aliasing bug in the VMR patch.

**Verified**: `testCloneFieldPriorities()` passes on Phvega.

### 2. Multi-arg `set()` methods now record priorities — VERIFIED FIX

`set(int, int, int)` and its overloads are rewritten to delegate to
`set(int, int)`:

```java
public final void set(int year, int month, int date) {
    set(YEAR, year);
    set(MONTH, month);
    set(DATE, date);
}
```

Each field now gets an incrementing priority, fixing the priority-zero
problem from the VMR patch.

**Verified**: `testMultiArgSetPriorities()` passes on Phvega.

### 3. DAY_OF_WEEK disambiguation simplified

The fragile cascading `if` blocks with the `aux` variable and the
`// Pattern 3 to match openjdk (?)` comment are replaced with a
`maxDowPatternP = max(womP, dowimP, woyP)` approach.

**Verified**: Not a bug in VMR either (VMR issue #7 was not a bug), but
the Phvega code is cleaner.

## Issues from VMR still present

### 1. `explicitDSTOffset` still removed — NOT INDEPENDENTLY TESTABLE

No DST invalidation logic is restored.  Same as VMR issue #3.

**Verified**: VMR's behavior matches OpenJDK in all tested scenarios.
See `REPORT-explicitDSTOffset.md`.

### 2. `if (isTimeSet) clear-all-isSet` still present — CONFIRMED BUG

The block that clears all `isSet[]` flags after time computation is
retained.  This causes HOUR+AM_PM values to be lost after a
`getTime()` + `set()` cycle.  Same as VMR issue #5.

**Verified**: `testHourAmPmPreservedAfterSet()` fails on Phvega with
the same 14-hour loss as VMR (hour reset to midnight).

**Test**: `CalendarFieldResolutionTest.testHourAmPmPreservedAfterSet()`

### 3. `areFieldsSet` still not invalidated in `set()` — NOT A BUG

Same as VMR issue #6.  Not a real bug because `get()` compensates.

### 4. Pattern 3 (DAY_OF_WEEK_IN_MONTH) calculation rewrite carried forward — CONFIRMED BUG

The VMR rewrite of negative `DAY_OF_WEEK_IN_MONTH` handling is kept
as-is.  Same as VMR issue #8.

**Verified**: `testNegativeDayOfWeekInMonth()` fails on Phvega with
the same results as VMR (off by one week).

**Test**: `CalendarTest.testNegativeDayOfWeekInMonth()`

### 5. Patterns 2 and 5 calculation rewrite carried forward — NOT A BUG

Same as VMR issue #9.  The rewritten formulas correctly account for
locale parameters.

## New issues introduced

### 1. `computeFields()` overwrites all priorities to `MINIMUM_PRIORITY` — NOT A BUG

```java
for (int i = 0; i < FIELD_COUNT; i++)
  {
    isSet[i] = true;
    fieldsPriority[i] = MINIMUM_PRIORITY;
  }
```

After any `get()` call triggers `computeFields()`, all 17 fields get
priority 1.  The initial review was concerned this would make all
patterns appear "complete", with computed fields participating in
resolution as if user-set.

**Verified**: Not a real bug.  This approach actually matches OpenJDK's
behavior, where `computeFields()` resets stamps to a computed level
(below user stamps).  After the reset, any user `set()` call gets a
higher priority, correctly winning over computed fields.

Testing with `get()` + `set()` scenarios showed Phvega matches OpenJDK
in 4 out of 5 cases (the remaining difference is caused by the
VMR-inherited isSet[] clearing bug, not the priority reset).

### 2. Incomplete-pattern fallback unconditionally picks pattern 3 — NOT A BUG

```java
if (best == UNSET)
  {
    best = maxDowPatternP;
    if (dowP != UNSET)
      {
        best = dowimP = dowP;
      }
  }
```

The initial review was concerned this would force pattern 3 when
`DAY_OF_WEEK` is set even if another pattern (e.g. pattern 2) should
win.

**Verified**: Not a real bug.  This fallback only triggers when ALL
pattern-determining field priorities are `UNSET` (0) except possibly
`DAY_OF_WEEK`.  In that degenerate case (e.g. only `DAY_OF_WEEK` set
after `clear()`), the choice is essentially arbitrary.  Testing showed
Phvega matches OpenJDK for all practical incomplete-pattern scenarios.

### 3. `fieldsPriority` changed to `protected` — STYLE ISSUE

The array is promoted from package-private to `protected`:

```java
protected int[] fieldsPriority = new int[FIELD_COUNT];
```

This is worse for encapsulation than the VMR version.  Any Calendar
subclass, including third-party ones, can now read and modify the
priority array directly.  Not a functional bug.

### 4. `rescalePriorities()` collapses equal priorities — NOT A BUG

The overflow-protection method `rescalePriorities()` (triggered at
`Integer.MAX_VALUE`) reassigns priorities in ascending order.  When
two fields share the same priority, both get the same new value,
collapsing their relative ordering.

**Verified**: Not a practical concern.  The only scenario creating
equal priorities is `computeFields()` setting all 17 to
`MINIMUM_PRIORITY`, which is correct behavior (all computed fields
should be equal).  `rescalePriorities()` would only trigger after
`Integer.MAX_VALUE` `set()` calls, which is not realistically
achievable.

### 5. Named constants add no value — STYLE ISSUE

`UNSET = 0` and `MINIMUM_PRIORITY = 1` are declared `protected static
final`, adding two new API-visible constants to `Calendar`.  These are
implementation details that should not be part of the protected API.
Not a functional bug.

## Summary

| #   | Issue                              | Verdict                    |
|-----|------------------------------------|----------------------------|
| **VMR fixes** | | |
| F1  | `clone()` fixed                    | Verified fix               |
| F2  | Multi-arg `set()` fixed            | Verified fix               |
| F3  | DOW disambiguation cleaned up      | Verified improvement       |
| **VMR inherited** | | |
| I1  | `explicitDSTOffset` removed        | Not independently testable |
| I2  | `isSet[]` clearing (HOUR+AM_PM)    | Confirmed bug, tested      |
| I3  | `areFieldsSet` not invalidated     | Not a bug                  |
| I4  | Negative DOWIM broken              | Confirmed bug, tested      |
| I5  | Patterns 2/5 rewrite               | Not a bug                  |
| **New issues** | | |
| N1  | `computeFields()` priority reset   | Not a bug (matches OpenJDK)|
| N2  | Pattern 3 fallback                 | Not a bug                  |
| N3  | `protected` field                  | Style issue                |
| N4  | `rescalePriorities()` collapsing   | Not a bug                  |
| N5  | Named constants                    | Style issue                |

**2 confirmed bugs (both VMR-inherited), 3 VMR fixes verified,
no new functional bugs introduced by Phvega.**
