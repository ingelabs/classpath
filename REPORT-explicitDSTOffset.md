# Report: explicitDSTOffset behavior

## Background

GNU Classpath's `Calendar.set()` maintains a boolean flag
`explicitDSTOffset`.  When the user calls `set(DST_OFFSET, value)`, the
flag is set to `true`.  Subsequent `set()` calls to other fields check
this flag: if `false`, they clear `isSet[DST_OFFSET]` (forcing
recomputation from the timezone); if `true`, they leave it alone.

In `GregorianCalendar.computeTime()`, the DST offset is resolved as:

```java
int dstOffset = isSet[DST_OFFSET] ? fields[DST_OFFSET]
                                  : (zone.getOffset(...) - rawOffset);
```

So an explicit `DST_OFFSET` is honored only if `isSet[DST_OFFSET]`
survives until `computeTime()` runs.

## The bug (before fix)

The original code never cleared `explicitDSTOffset` after
`computeTime()` consumed the value.  Once set, the flag remained `true`
indefinitely (only `clear()` reset it).  This meant that after a
`getTime()` + `set(other field)` cycle, `isSet[DST_OFFSET]` was still
protected, and the stale explicit value was reused instead of being
recomputed from the timezone.

## OpenJDK approach

OpenJDK uses a three-level stamp system to distinguish field states:

- `UNSET = 0`: field has never been set
- `COMPUTED = 1`: field was computed (by `computeFields()`)
- `MINIMUM_USER_STAMP = 2` and above: field was explicitly set by the user

When `set(DST_OFFSET, value)` is called, the field receives a stamp
value `>= MINIMUM_USER_STAMP` (the stamp counter increments with each
`set()` call, starting from 2).

### The stamp reset mechanism

The key to understanding the behavior is the chain of events triggered
by `getTime()` followed by `set()`:

1. **`getTime()` calls `updateTime()`**, which calls `computeTime()`.
   At the end of `GregorianCalendar.computeTime()` (line 2830 in
   JDK 11), the method calls:

   ```java
   setFieldsNormalized(YEAR_MASK | MONTH_MASK | ...);
   ```

   This sets `areFieldsSet = true` and `areAllFieldsSet = false`.
   Critically, it does **not** reset stamps at this point.

2. **The next `set()` call** triggers a deferred `computeFields()`.
   In `Calendar.set()`, there is a guard:

   ```java
   if (areFieldsSet && !areAllFieldsSet) {
       computeFields();
   }
   ```

   Because `setFieldsNormalized()` left `areFieldsSet = true` and
   `areAllFieldsSet = false`, this guard fires on the very first
   `set()` call after `getTime()`.

3. **`computeFields()` calls `setFieldsComputed(ALL_FIELDS)`**, which
   resets **all** stamps to `COMPUTED = 1`:

   ```java
   for (int i = 0; i < fields.length; i++) {
       stamp[i] = COMPUTED;   // = 1
       isSet[i] = true;
   }
   ```

   After this, every field — including `DST_OFFSET` — has
   `stamp = 1`, which is below `MINIMUM_USER_STAMP = 2`.

4. **The subsequent `set(DAY_OF_MONTH, 20)` call** (or whatever field
   is being modified) then records `stamp[DAY_OF_MONTH] = nextStamp++`
   (a value >= 2).  But `stamp[DST_OFFSET]` remains at 1.

5. **The second `getTime()` call** invokes `computeTime()`, which
   calls `selectFields()` to determine which fields are user-set.
   For `DST_OFFSET`, `selectFields()` uses a **deliberate** check:

   ```java
   fieldMask |= (stamp[DST_OFFSET] >= MINIMUM_USER_STAMP) ?
                 DST_OFFSET_MASK : 0;
   ```

   Note the use of `>= MINIMUM_USER_STAMP` (not `!= UNSET`).  This
   intentionally distinguishes "computed" (stamp=1) from "user-set"
   (stamp>=2).  Since `stamp[DST_OFFSET] = 1 < 2`, the field is
   **not** included in the mask, and `computeTime()` recomputes the
   DST offset from the timezone.

## VMR approach

The VMR patch removes the `explicitDSTOffset` flag and all associated
logic in `set()`.  The `isSet[DST_OFFSET]` flag is no longer protected
against clearing by subsequent `set()` calls.

VMR also retains the `if (isTimeSet) clear-all-isSet` block in
`set()`, which clears *all* `isSet[]` flags on the first `set()` after a
`getTime()` call.  This means `isSet[DST_OFFSET]` is always cleared
after a computation cycle, which accidentally matches OpenJDK's
behavior for DST_OFFSET.

However, the sledgehammer clearing of all `isSet[]` flags causes
collateral damage to other fields.  For example, after `getTime()` +
`set(MONTH)`, the `isSet[HOUR]` and `isSet[AM_PM]` flags are also
cleared, causing HOUR and AM_PM to revert to defaults instead of being
preserved.  This is tested by `testHourAmPmPreservedAfterSet` in
CalendarTest.

## Our fix

We keep the `explicitDSTOffset` flag but clear it at the top of
`set()` when `isTimeSet` is `true` — i.e., when `computeTime()` has
already consumed the explicit value:

```java
public void set(int field, int value)
{
    // If computeTime() already ran (isTimeSet == true), it consumed
    // any explicit DST_OFFSET.  Clear the flag so that DST_OFFSET
    // reverts to "computed" status and will be invalidated below.
    if (isTimeSet)
      explicitDSTOffset = false;

    isTimeSet = false;
    areFieldsSet = false;
    fields[field] = value;
    isSet[field] = true;
    stamp[field] = nextStamp++;

    if (field == DST_OFFSET)
      explicitDSTOffset = true;

    if (! explicitDSTOffset && (field != DST_OFFSET && field != ZONE_OFFSET))
      {
        isSet[DST_OFFSET] = false;
        stamp[DST_OFFSET] = 0;
      }
}
```

The logic:

1. On the first `set()` after `getTime()`, `isTimeSet` is `true`,
   so `explicitDSTOffset` is cleared.
2. If this `set()` call is for `DST_OFFSET` itself, the flag is
   immediately re-set to `true`.
3. If this `set()` call is for any other field, `explicitDSTOffset`
   is now `false`, so the existing guard clears `isSet[DST_OFFSET]`
   and `stamp[DST_OFFSET]`, forcing recomputation on the next cycle.

This is surgically targeted: only `DST_OFFSET` is affected, unlike
VMR's approach which clears all `isSet[]` flags.  The flag and all
its logic remain private to `Calendar`, with no changes needed in
`GregorianCalendar`.

### Comparison with VMR

For DST_OFFSET specifically, both approaches produce the same result.
The difference is scope:

| Aspect | VMR | Our fix |
|--------|-----|---------|
| Mechanism | `if (isTimeSet)` clears **all** `isSet[]` | `if (isTimeSet)` clears only `explicitDSTOffset`, then existing guard clears `isSet[DST_OFFSET]` |
| DST_OFFSET result | Correct (matches OpenJDK) | Correct (matches OpenJDK) |
| Collateral damage | Yes — HOUR, AM_PM, and other fields lose their `isSet` status | None — only DST_OFFSET is affected |
| Match with OpenJDK | Accidental (right result, wrong mechanism) | Deliberate (targeted clearing after consumption) |

## Test results

Three scenarios were tested with Europe/Madrid timezone (CET/CEST),
setting a July date at 12:00 with `DST_OFFSET=0` (forcing CET instead
of CEST):

### Test A: immediate getTime()

```java
c.set(YEAR, 2018); c.set(MONTH, JULY); c.set(DAY_OF_MONTH, 15);
c.set(HOUR_OF_DAY, 12); c.set(DST_OFFSET, 0);
c.getTime();  // 12:00 CET = 11:00 UTC
```

| Implementation | Result (UTC millis) | DST_OFFSET used |
|----------------|--------------------:|-----------------|
| OpenJDK        |       1531652400000 | 0 (explicit)    |
| VMR            |       1531652400000 | 0 (explicit)    |
| Our fix        |       1531652400000 | 0 (explicit)    |

All agree.  The explicit `DST_OFFSET=0` is honored.

### Test B: getTime() + set(DAY_OF_MONTH) + getTime()

```java
c.set(YEAR, 2018); c.set(MONTH, JULY); c.set(DAY_OF_MONTH, 15);
c.set(HOUR_OF_DAY, 12); c.set(DST_OFFSET, 0);
c.getTime();                        // triggers computeTime()
c.set(DAY_OF_MONTH, 20);           // modify a date field
c.getTime();                        // is DST_OFFSET=0 still honored?
```

| Implementation | Result (UTC millis) | DST_OFFSET used |
|----------------|--------------------:|-----------------|
| OpenJDK        |       1532080800000 | 3600000 (recomputed, CEST) |
| VMR            |       1532080800000 | 3600000 (recomputed, CEST) |
| Our fix        |       1532080800000 | 3600000 (recomputed, CEST) |

All agree.  After the `getTime()` + `set()` cycle, `DST_OFFSET` is
recomputed from the timezone.

### Test C: DST_OFFSET re-set after the cycle

```java
c.set(YEAR, 2018); c.set(MONTH, JULY); c.set(DAY_OF_MONTH, 15);
c.set(HOUR_OF_DAY, 12);
c.getTime();
c.set(DAY_OF_MONTH, 20);
c.set(DST_OFFSET, 0);              // explicitly re-set after the cycle
c.getTime();
```

| Implementation | Result (UTC millis) | DST_OFFSET used |
|----------------|--------------------:|-----------------|
| OpenJDK        |       1532084400000 | 0 (explicit)    |
| VMR            |       1532084400000 | 0 (explicit)    |
| Our fix        |       1532084400000 | 0 (explicit)    |

All agree.  Re-setting `DST_OFFSET` after the cycle works everywhere.

## Stamp trace evidence

Using reflection to trace stamp values through the lifecycle on
OpenJDK 11 (`/tmp/TestStamps.java`):

```
After set(DST_OFFSET,0): stamp[DST_OFFSET]=6 stamp[DAY_OF_MONTH]=4 fields[DST_OFFSET]=0
getTimeInMillis() = 1531652400000
After getTime():         stamp[DST_OFFSET]=1 stamp[DAY_OF_MONTH]=1 fields[DST_OFFSET]=0
After set(DOM,20):       stamp[DST_OFFSET]=1 stamp[DAY_OF_MONTH]=2 fields[DST_OFFSET]=3600000
getTimeInMillis() = 1532080800000
After 2nd getTime():     stamp[DST_OFFSET]=1 stamp[DAY_OF_MONTH]=1 fields[DST_OFFSET]=3600000
```

Key observations:

- After `set(DST_OFFSET, 0)`: stamp is 6 (user-set, >= MINIMUM_USER_STAMP=2).
- After `getTime()`: stamp drops to 1 (COMPUTED).  This is the
  `setFieldsComputed()` call triggered by the deferred `computeFields()`
  inside the first `set()` after `getTime()`.  Note that the actual
  stamp reset happens during the `set(DAY_OF_MONTH, 20)` call, not
  during `getTime()` itself.
- After `set(DOM, 20)`: `stamp[DAY_OF_MONTH]` is now 2 (user-set),
  while `stamp[DST_OFFSET]` remains 1 (computed).  The `fields[]` value
  for DST_OFFSET has already changed to 3600000 (CEST) because
  `computeFields()` recomputed it.
- After second `getTime()`: DST_OFFSET=3600000 is used (CEST),
  confirming that `stamp[DST_OFFSET]=1 < MINIMUM_USER_STAMP=2` caused
  recomputation from the timezone.

Boolean flag trace (`/tmp/TestFlags.java`):

```
After clear():            areFieldsSet=false isTimeSet=false areAllFieldsSet=false
After sets:               areFieldsSet=false isTimeSet=false areAllFieldsSet=false
After getTimeInMillis():  areFieldsSet=true  isTimeSet=true  areAllFieldsSet=false
After set(DOM,20):        areFieldsSet=true  isTimeSet=false areAllFieldsSet=true
```

The transition from `areAllFieldsSet=false` (after getTime) to
`areAllFieldsSet=true` (after set) confirms that `computeFields()` ran
inside the `set()` call, triggered by the `areFieldsSet && !areAllFieldsSet`
guard.

## JDK bug tracker evidence

- **JDK-6615045** ("Calendar.set(SECOND, ...) clears DST_OFFSET set by
  user"): Closed as **"Not an Issue"** (2007).  The reporter expected
  `set(DST_OFFSET)` to survive across `getTime()` + `set()` cycles,
  but Sun engineers confirmed that this is expected behavior: computed
  fields are re-derived after a computation cycle.

- **JDK-4312621** ("Calendar doesn't handle DST transition"): Remains
  **unresolved**.  Acknowledges that the Calendar API has an
  architectural gap for DST transition disambiguation.  The
  `DST_OFFSET` field was designed as an output of `computeFields()`
  rather than a reliable input to `computeTime()`.

## Conclusion

1. **OpenJDK's behavior is by design.**  The three-level stamp system
   with the `COMPUTED`/`MINIMUM_USER_STAMP` distinction, the
   `setFieldsNormalized()` -> deferred `computeFields()` chain, and the
   `selectFields()` check are all deliberate mechanisms.  The JDK bug
   tracker (JDK-6615045, closed as "Not an Issue") confirms that
   `DST_OFFSET` is not expected to survive across `getTime()` + `set()`
   cycles.

2. **Fixed.**  Our `set()` now clears `explicitDSTOffset` when
   `isTimeSet` is `true`, matching OpenJDK's behavior in all three
   test scenarios.  The fix is minimal (4 lines in `Calendar.set()`),
   keeps the flag private to `Calendar`, and causes no collateral
   damage to other fields.

3. **Test coverage.**  The three scenarios (A, B, C) are covered by
   `testExplicitDSTOffset()` in `CalendarTest.java` (malva), verified
   against both OpenJDK and our implementation.
