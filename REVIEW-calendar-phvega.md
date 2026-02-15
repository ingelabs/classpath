# Review: Phvega Calendar changes (commit2, on top of VMR)

## Overview

The Phvega patch is a fixup on top of the VMR implementation.  It
addresses some of the issues found in the VMR patch, introduces named
constants and an overflow-protection mechanism, but also introduces new
problems.

## Issues fixed from VMR

### 1. `clone()` now copies `fieldsPriority`

The `clone()` method is updated to deep-copy the array:

```java
cal.fieldsPriority = fieldsPriority.clone();
```

This fixes the aliasing bug in the VMR patch.

### 2. Multi-arg `set()` methods now record priorities

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

### 3. DAY_OF_WEEK disambiguation simplified

The fragile cascading `if` blocks with the `aux` variable and the
`// Pattern 3 to match openjdk (?)` comment are replaced with a
`maxDowPatternP = max(womP, dowimP, woyP)` approach.

## Issues from VMR still present

### 1. `explicitDSTOffset` still removed

No DST invalidation logic is restored.

### 2. `if (isTimeSet) clear-all-isSet` still present in `set()`

The block that clears all `isSet[]` flags after time computation is
retained.  See VMR issue #5.

### 3. `areFieldsSet` still not invalidated in `set()`

See VMR issue #6.

### 4. Pattern 2, 3, 5 calculation rewrites carried forward

The rewritten day-calculation code from the VMR patch is kept as-is.

## New issues introduced

### 1. `computeFields()` overwrites all priorities to `MINIMUM_PRIORITY`

```java
for (int i = 0; i < FIELD_COUNT; i++)
  {
    isSet[i] = true;
    fieldsPriority[i] = MINIMUM_PRIORITY;
  }
```

After any `getTime()` or `get()` call triggers `computeFields()`, all
17 fields get priority 1.  This means **all five date patterns are
always complete** after any field computation, because every field has a
non-zero priority.

A subsequent `set(WEEK_OF_YEAR, 10)` gives that field a high priority,
but patterns 1, 2, 3 also have all their fields at priority 1, making
them "complete" competitors.  The resolution degenerates into comparing
individual field priorities, which mostly works by accident, but
fundamentally changes the semantics: computed fields participate in
resolution as if the user had set them.

Combined with issue #2 from VMR (the `isSet[]` clearing), the behavior
becomes contradictory: `isSet[]` says only one field is set (incomplete
patterns), while `fieldsPriority[]` says all 17 fields are set (all
patterns complete).  The resolution code checks both, making the
outcome depend on which check runs first.

### 2. Incomplete-pattern fallback unconditionally picks pattern 3

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

When no complete pattern is found and `DAY_OF_WEEK` is set, this forces
`best = dowimP = dowP`, unconditionally selecting pattern 3.  For
example, if only `WEEK_OF_MONTH` and `DAY_OF_WEEK` are set, pattern 2
should win (`maxDowPatternP` would be `womP`), but the `dowP != UNSET`
branch overrides it to `dowimP`.

### 3. `fieldsPriority` changed to `protected`

The array is promoted from package-private to `protected`:

```java
protected int[] fieldsPriority = new int[FIELD_COUNT];
```

This is worse for encapsulation than the VMR version.  Any Calendar
subclass, including third-party ones, can now read and modify the
priority array directly, bypassing any invariants maintained by `set()`
and `clear()`.

### 4. `rescalePriorities()` collapses equal priorities

The overflow-protection method `rescalePriorities()` (triggered at
`Integer.MAX_VALUE`) reassigns priorities in ascending order.  However,
when two fields share the same priority value, both are assigned the
same new value, collapsing their relative ordering:

```java
for (int i = 0; i < fieldsPriority.length; i++)
  {
    if (fieldsPriority[i] == min)
      fieldsPriority[i] = newPriority;
  }
newPriority++;
```

In practice, direct `set()` calls produce unique priorities via
`nextPriority++`, making collisions unlikely.  But `computeFields()`
(from issue #1 above) sets all 17 fields to `MINIMUM_PRIORITY`,
creating exactly the collision scenario.  If `rescalePriorities()` runs
after that, all 17 computed fields collapse to a single priority value,
making it impossible to distinguish among them.

### 5. Named constants add no value

`UNSET = 0` and `MINIMUM_PRIORITY = 1` are declared `protected static
final`, adding two new API-visible constants to `Calendar`.  These are
implementation details that should not be part of the protected API.
The values 0 and 1 were already clear in context.
