# Bug Report: Calendar field resolution rules not implemented

## Bug description

`java.util.Calendar` does not implement the **field resolution rules**
described in the API specification.  When multiple, conflicting calendar
fields are set, the spec says:

> If there is any conflict in calendar field values, Calendar gives
> priorities to calendar fields that have been set more recently.  The
> most recent combination, as determined by the most recently set single
> field, will be used.

GNU Classpath instead uses an **eager-invalidation** scheme inside
`Calendar.set(int, int)`: a large `switch` statement clears the `isSet[]`
flags of competing fields the moment a new field is set.  This destroys
the information needed to determine *which* field was set most recently,
making correct resolution impossible.

### The five date-field combinations (spec)

| # | Combination |
|---|-------------|
| 1 | YEAR + MONTH + DAY\_OF\_MONTH |
| 2 | YEAR + MONTH + WEEK\_OF\_MONTH + DAY\_OF\_WEEK |
| 3 | YEAR + MONTH + DAY\_OF\_WEEK\_IN\_MONTH + DAY\_OF\_WEEK |
| 4 | YEAR + DAY\_OF\_YEAR |
| 5 | YEAR + DAY\_OF\_WEEK + WEEK\_OF\_YEAR |

And two time-of-day combinations:

| # | Combination |
|---|-------------|
| 1 | HOUR\_OF\_DAY |
| 2 | AM\_PM + HOUR |

### Example of incorrect behaviour (before fix)

```java
Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
c.clear();
c.set(Calendar.YEAR, 2018);
c.set(Calendar.DAY_OF_YEAR, 50);       // pattern 4 — complete
c.set(Calendar.WEEK_OF_YEAR, 3);       // pattern 5 — incomplete (no DAY_OF_WEEK)
c.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2); // pattern 3 — incomplete (no DAY_OF_WEEK)
// Expected: 2018-02-19 (pattern 4: DAY_OF_YEAR=50)
// Actual:   2018-01-14 (pattern 3 with DAY_OF_WEEK defaulted to Sunday)
```

The old code's `set(WEEK_OF_YEAR, 3)` immediately cleared
`isSet[DAY_OF_YEAR]`, so by the time `computeTime()` ran, the only
"surviving" pattern was #3 — even though DAY\_OF\_YEAR formed a fully
complete pattern and should have won.

## Root cause

`Calendar.set(int, int)` contained a `switch` statement (lines 808–879)
that eagerly cleared `isSet[]` flags of competing fields.  For example,
setting `WEEK_OF_YEAR` cleared `isSet[DAY_OF_YEAR]`,
`isSet[DAY_OF_MONTH]`, etc.  This approach:

1. **Loses ordering information** — there is no way to tell which field
   was set most recently, only which fields are "currently valid".
2. **Incorrectly resolves conflicts** — the last `set()` call's `switch`
   case decides the winner, rather than a proper priority evaluation of
   all set fields.
3. **Silently injects defaults** — e.g. `set(WEEK_OF_MONTH, n)` would
   force `fields[DAY_OF_WEEK] = getFirstDayOfWeek()` if DAY\_OF\_WEEK
   was not already set, conflating "defaulted" with "explicitly set".

OpenJDK solves this with a **stamp mechanism**: each `set()` call records
an incrementing stamp value; `computeTime()` evaluates all five patterns
and picks the one whose determining fields have the highest aggregate
stamp.

## Fix

### Calendar.java

Added a `stamp[]` array and `nextStamp` counter to track the order in
which fields are set:

```java
private int[] stamp = new int[FIELD_COUNT];
private int nextStamp = 2;  // 0 = unset, 1 = reserved, ≥2 = user-set
```

Added package-private helper methods for `GregorianCalendar`:

- `getStamp(int field)` — returns the stamp for a field.
- `aggregateStamp(int... fields)` — returns 0 if any field is unset
  (stamp == 0); otherwise returns the maximum stamp.  This tells whether
  a field combination is "complete" and how recently its fields were set.

Simplified `set(int field, int value)`:

- **Removed** the `if (isTimeSet) clear-all-isSet` block.
- **Removed** the entire `switch` statement that eagerly invalidated
  competing fields.
- Now simply records the value, sets `isSet[field] = true`, and assigns
  `stamp[field] = nextStamp++`.
- Kept DST\_OFFSET invalidation logic (clearing stamp and isSet when a
  non-timezone field is set and DST was not explicitly set).

Updated the multi-argument `set()` overloads (`set(y,m,d)`,
`set(y,m,d,h,min)`, `set(y,m,d,h,min,s)`) to record stamps for each
field they set.

Updated `clear()` to reset all stamps to 0 and `nextStamp` to 2.
Updated `clear(int)` to reset the specific field's stamp.
Updated `clone()` to clone the stamp array.

### GregorianCalendar.java

Added a `resolveDateFields()` method that implements stamp-based
resolution.  The algorithm uses two groups:

- **Group 1** (month-based): patterns 1, 2, 3.
  Determining fields (excluding the common YEAR/MONTH):
  - Pattern 1: `DAY_OF_MONTH`
  - Pattern 2: `WEEK_OF_MONTH` + `DAY_OF_WEEK`
  - Pattern 3: `DAY_OF_WEEK_IN_MONTH` + `DAY_OF_WEEK`

- **Group 2** (day-of-year-based): patterns 4, 5.
  Determining fields:
  - Pattern 4: `DAY_OF_YEAR`
  - Pattern 5: `WEEK_OF_YEAR` + `DAY_OF_WEEK`

Resolution rules:

1. Within each group, compute the aggregate stamp of each pattern's
   determining fields.  The pattern with the highest aggregate wins; ties
   broken by the later-listed pattern (`>=` comparison).
2. Between groups, group 2 must **strictly exceed** group 1 to win; ties
   go to group 1.  This matches the spec's ordering where month-based
   patterns have higher default priority.
3. If no pattern is fully complete (all determining fields set), a
   fallback selects the pattern whose primary field has the highest
   individual stamp.

Updated `computeTime()`:

- Calls `resolveDateFields()` to determine which date pattern to use,
  then dispatches to the appropriate calculation via a `switch`.
- `DAY_OF_WEEK` defaults to `getFirstDayOfWeek()` when not explicitly
  set, applied at resolution time rather than eagerly in `set()`.
- Hour resolution uses stamp comparison:
  `aggregateStamp(AM_PM, HOUR) > getStamp(HOUR_OF_DAY)` to pick
  AM/PM+HOUR over HOUR\_OF\_DAY when the former was set more recently.

## Testing

All 20 test cases in `CalendarFieldResolutionTest` pass (8 inconsistent-
data tests + 12 incomplete-pattern tests).  The full malva test suite
(27 tests) passes with no regressions.

## Files changed

| File | Lines added | Lines removed |
|------|-------------|---------------|
| `java/util/Calendar.java` | 79 | 90 |
| `java/util/GregorianCalendar.java` | 198 | 78 |
