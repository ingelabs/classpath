# Review: VMR Calendar implementation (commit1)

## Overview

The VMR implementation replaces the eager-invalidation logic in
`Calendar.set()` with a priority-tracking array (`fieldsPriority`),
and rewrites the date-pattern resolution in
`GregorianCalendar.computeTime()` to use those priorities.  The core
idea is correct and addresses the same root cause identified
independently in our fix.

This document lists the issues found in the patch, along with
verification results from testing against OpenJDK and our fix.

## Issues

### 1. `clone()` not updated — CONFIRMED BUG

`Calendar.clone()` copies `fields` and `isSet` but is not updated to
copy `fieldsPriority`.  After cloning, both the original and the clone
share the same `fieldsPriority` array.  Any subsequent `set()` call on
either calendar corrupts the other.

**Verified**: When both patterns 1 and 2 are complete and DAY_OF_MONTH
was set last, cloning and calling `set()` on the clone causes the
original to resolve via pattern 2 instead of pattern 1.  VMR gives
Jan 12 (pattern 2) instead of Jan 15 (pattern 1).

**Test**: `CalendarFieldResolutionTest.testCloneFieldPriorities()`

### 2. Multi-arg `set()` methods do not record priorities — CONFIRMED BUG

`set(int year, int month, int date)` and its overloads directly assign
to `fields[]` and `isSet[]` without recording entries in
`fieldsPriority`.  Fields set through these methods have priority 0
(indistinguishable from unset), so any subsequent single-field `set()`
call will always win regardless of intent.

This affects the `GregorianCalendar(int, int, int, ...)` constructors,
which delegate to these methods.

**Verified**: Setting WEEK_OF_YEAR + DOW, then calling `set(y, m, d)`,
then re-setting DOW should make pattern 5 win (DOW is the most recent
distinguishing field).  VMR gives Jan 15 (pattern 1, from the
multi-arg set) instead of Mar 9 (pattern 5).

**Test**: `CalendarFieldResolutionTest.testMultiArgSetPriorities()`

### 3. `explicitDSTOffset` removed without replacement — NOT INDEPENDENTLY TESTABLE

The original `explicitDSTOffset` flag ensured that a user-provided
`DST_OFFSET` was not silently overridden by the automatic DST
computation in `computeTime()`.  The VMR patch removes the flag and all
associated DST-invalidation logic in `set()`.

**Verified**: VMR's behavior matches OpenJDK in all tested scenarios.
The `if (isTimeSet) clear-all-isSet` block in `set()` clears
`isSet[DST_OFFSET]` after any computation cycle, which accidentally
produces the same result as OpenJDK's stamp-based downgrading in
`computeFields()`.

See `REPORT-explicitDSTOffset.md` for detailed analysis.

### 4. `fieldsPriority` is package-private — SKIPPED

The array is declared with no access modifier:

```java
int[] fieldsPriority = new int[FIELD_COUNT];
```

This exposes internal state to every class in `java.util`.  It should be
`private`, with accessor methods for `GregorianCalendar`.

Not tested (visibility/style issue, not a functional bug).

### 5. `if (isTimeSet) clear-all-isSet` retained in `set()` — CONFIRMED BUG

The original block that clears all `isSet[]` flags when `set()` is
called after time was computed is retained:

```java
if (isTimeSet)
  for (int i = 0; i < FIELD_COUNT; i++)
    isSet[i] = false;
```

After any `getTime()` or `get()` call (which sets `isTimeSet = true`),
the next `set()` clears all `isSet[]` flags.  This destroys the
time pattern resolution: when time was set via HOUR + AM_PM, clearing
`isSet[HOUR]` and `isSet[AM_PM]` causes `computeTime()` to fall
through to `fields[HOUR_OF_DAY]`, which is 0, silently resetting the
hour to midnight.

**Verified**: Set time via HOUR + AM_PM (2:30 PM = 14:30), call
`getTime()`, then `set(MONTH, MARCH)`, then `getTime()`.  VMR gives
00:30 (hour lost, 14 hours missing) instead of 14:30.  Confirmed with
three variants: set(MONTH), set(DAY_OF_MONTH), and set(YEAR) all
trigger the same bug.

**Test**: `CalendarFieldResolutionTest.testHourAmPmPreservedAfterSet()`

### 6. `areFieldsSet` not invalidated in `set()` — NOT A BUG

`set()` marks `isTimeSet = false` but does not set
`areFieldsSet = false`.  The concern was that `complete()` would skip
`computeFields()`, leaving stale computed values in the `fields[]`
array.

**Verified**: Not a real bug.  VMR's `get()` has a compensating safety
check: `if (!isSet[field]) areFieldsSet = false;`.  Since the
`isTimeSet` clearing block in `set()` clears all `isSet[]` flags, any
subsequent `get()` call will find `isSet[field] == false`, set
`areFieldsSet = false`, and trigger `computeFields()` via `complete()`.
The end result is correct recomputation.

### 7. DAY_OF_WEEK disambiguation is fragile — NOT A BUG

The initial review described a cascading if-else structure that would
give pattern 2 an unfair advantage over patterns 3 and 5 when
DAY_OF_WEEK has the highest priority and is shared by all three.

**Verified**: Not a real bug.  The described cascading if-else code does
not exist in VMR.  The actual code resolves ties by comparing the
priorities of the distinguishing fields (WEEK_OF_MONTH vs
DAY_OF_WEEK_IN_MONTH vs WEEK_OF_YEAR) using `fieldsPriority[]`, which
correctly picks the pattern whose unique field was set most recently.

### 8. Pattern 3 (DAY_OF_WEEK_IN_MONTH) calculation rewritten — CONFIRMED BUG

The original code handles negative `DAY_OF_WEEK_IN_MONTH` values
(e.g. -1 = last occurrence in month) by incrementing the month and
counting backwards.  The VMR patch replaces this with modular
arithmetic (`% weeks`) and a special case for value 0.  This changes
the semantics for negative values.

**Verified**: Last Friday of January 2018 should be Jan 26; VMR gives
Jan 19 (off by one week).  Last Wednesday of February 2018 should be
Feb 28; VMR gives Feb 21.  Second-to-last Friday of January 2018
should be Jan 19; VMR gives Jan 12.

**Test**: `CalendarTest.testNegativeDayOfWeekInMonth()`

### 9. Patterns 2 and 5 (WEEK_OF_MONTH / WEEK_OF_YEAR) calculation rewritten — NOT A BUG

The original day-calculation code for patterns 2 and 5 is replaced with
a different algorithm.  The initial review suggested the new code might
not account for `getFirstDayOfWeek()` and `getMinimalDaysInFirstWeek()`.

**Verified**: Not a real bug.  Testing with 38 combinations of
different first-day-of-week and minimal-days settings showed VMR
produces the same results as OpenJDK in all cases.  The rewritten
formulas correctly incorporate locale-dependent week parameters.

## Summary

| #   | Issue                              | Verdict                    |
|-----|------------------------------------|----------------------------|
| 1   | `clone()` not updated              | Confirmed bug, tested      |
| 2   | Multi-arg `set()` no priorities    | Confirmed bug, tested      |
| 3   | `explicitDSTOffset` removed        | Not independently testable |
| 4   | Package-private field              | Skipped (style issue)      |
| 5   | `isSet[]` clearing loses HOUR+AM_PM| Confirmed bug, tested      |
| 6   | `areFieldsSet` not invalidated     | Not a bug                  |
| 7   | DAY_OF_WEEK disambiguation         | Not a bug                  |
| 8   | Negative DAY_OF_WEEK_IN_MONTH      | Confirmed bug, tested      |
| 9   | Patterns 2/5 rewritten             | Not a bug                  |

**4 confirmed bugs with test cases, 1 not independently testable,
1 skipped, 3 not actual bugs.**
