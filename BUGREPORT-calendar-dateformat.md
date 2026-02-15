# Calendar / DateFormat Related Bugs

Bugs from `bugs_classpath_20260215.xml` related to Calendar, GregorianCalendar,
DateFormat, SimpleDateFormat, TimeZone, and other date/time classes.

Source code analyzed: GNU Classpath `master` (commit be6a181d9).
Runtime verified with JamVM 2.0.1 + GNU Classpath master glibj.zip.

## Calendar / GregorianCalendar

### Bug 22785 -- Calendar/getMinimalDaysInFirstWeek() problem for some locales
**Status: STILL PRESENT**

`Calendar.getMinimalDaysInFirstWeek()` returns 1 for `Locale.UK` (country=GB)
but should return 4 per CLDR data. The Calendar constructor reads from
`resource/java/util/weeks.properties`, which has no `minDays.GB` entry, so it
falls back to `minDays.DEFAULT=1`. The UK entry is simply missing from the
properties file.

### Bug 25944 -- Calendar: Difference between Sun and Classpath while crossing DST "back"
**Status: STILL PRESENT**

`Calendar.add(DAY_OF_MONTH, 1)` across the autumn DST transition produces wrong
results: dates repeat and times drift by one hour. Root cause:
`GregorianCalendar.add()` for day fields (line 992) does
`time += amount * (24 * 60 * 60 * 1000L)` -- adding exactly 24 hours in
milliseconds. This doesn't account for DST transitions where a day may be 23 or
25 hours. The JDK implementation properly adjusts for DST by working at the
field level.

### Bug 29149 -- GregorianCalendar problem with daylight-saving(?)
**Status: STILL PRESENT (duplicate of 25944)**

Same root cause as 25944. `add(DAY_OF_YEAR, 1)` across autumn DST boundary
produces wrong dates for the same reason: hardcoded 24-hour millisecond
arithmetic.

### Bug 29690 -- GregorianCalendar implementation disagrees with itself
**Status: FIXED (verified by runtime test)**

Non-lenient mode no longer throws `IllegalArgumentException("Illegal
WEEK_OF_YEAR")` for valid dates like December 30, 2001. Runtime test with
JamVM + clean master glibj.zip confirms `calendar.getTime()` succeeds.

### Bug 31784 -- GregorianCalendar.add() broken
**Status: FIXED (verified by runtime test)**

`add(Calendar.YEAR, 1)` no longer changes the day of month. Runtime test with
JamVM + clean master glibj.zip confirms July 5, 2000 + add(YEAR,1) =
July 5, 2001.

### Bug 32443 -- Calendar.set(HOUR_OF_DAY) makes time jump back or forward
**Status: STILL PRESENT**

Calling `set(HOUR_OF_DAY, 0)` after setting a date via `set(y,m,d,h,m,s)` also
changes the date. Root cause: `Calendar.set()` at line 795 clears ALL `isSet[]`
flags when `isTimeSet` is true. After clearing, only `isSet[HOUR_OF_DAY]` is
true. When `computeTime()` runs, `!isSet[MONTH]` is true (line 523), so it
enters the wrong code path that uses `fields[DAY_OF_YEAR]` (line 550) -- a stale
value from a previous `complete()` call that may correspond to a different date
entirely.

## DateFormat / SimpleDateFormat

### Bug 28549 -- SimpleDateFormat's timezone output wrong
**Status: STILL PRESENT (resource data issue)**

`SimpleDateFormat` with `"zzz"` pattern produces long timezone names or
GMT-offset strings instead of standard abbreviations. This is a timezone
resource data quality issue -- the `DateFormatSymbols` zone strings don't contain
proper short abbreviations for many timezones.

### Bug 30359 -- SimpleDateFormat parse "invalid Date syntax in" exception
**Status: FIXED**

The original IKVM-specific part was fixed (a `String.contains()` bug matching
`0xFFFD`). The parse failures for year-end dates in non-lenient mode (e.g.,
`"31/12/yyyy"` for December 31) are also fixed -- the `nonLeniencyCheck`
WEEK_OF_YEAR issue (bug 29690/64174) is resolved.

### Bug 32278 -- java.text.DateFormat.parse error
**Status: FIXED**

The bug reports `DateFormat.parse` failures for iCalendar-format strings
(`"19700329T020000"`, `"20070527T163830Z"`). The ical4j library uses patterns
`"yyyyMMdd'T'HHmmss"` and `"yyyyMMdd'T'HHmmss'Z'"`. Tracing through the
current `parse()` method: quoted literal handling (lines 910-922) correctly
toggles `quote_start`, characters inside quotes are matched via `expect()`
(line 946), the `limit_digits` logic (lines 962-966) correctly limits digit
consumption for adjacent numeric fields (`yyyy`→`MM`→`dd`, `HH`→`mm`→`ss`),
and the non-limited `dd` field stops at the non-digit `T`. Both inputs parse
successfully with the current code. The bug was filed against gcj/Fedora 7
(classpath ~0.93) and was likely caused by the `NumberFormat.parse()` digit
consumption bugs that were also the root cause of bug 30359.

### Bug 39789 -- SimpleDateFormat.parse returns incorrect Date
**Status: STILL PRESENT (verified by runtime test)**

`SimpleDateFormat.parse()` succeeds when it should fail (missing timezone in
input but required by format `"Z"`). `parse("2009-04-14T20:18:10")` with
pattern `"yyyy-MM-dd'T'HH:mm:ssZ"` returns a Date instead of throwing
`ParseException`. Root cause: `computeOffset()` correctly returns null for the
empty remaining input, but the zone string lookup (lines 1070-1093) matches
because many zone strings in `DateFormatSymbols.getZoneStrings()` are empty
strings (`""`), and `dateStr.startsWith("", index)` always returns true. This
causes `found_zone = true` on a spurious empty-string match, so the check at
lines 1095-1098 is bypassed.

### Bug 87590 -- SimpleDateFormat parse returns incorrect date if weekday is parsed
**Status: STILL PRESENT**

When the format pattern includes `EEE` (weekday) alongside `dd` (day of month),
the parsed date is wrong. For input `"2010/10/27 10:00:00 Wed"` with pattern
`"yyyy/MM/dd HH:mm:ss EEE"`, the result is Sep 29 instead of Oct 27. Root
cause: `calendar.set(DAY_OF_WEEK, 4)` during parsing sets `isSet[DAY_OF_WEEK]`
to true. In `computeTime()`, `isSet[DAY_OF_WEEK]` being true (line 555) causes
priority 2 resolution (WEEK_OF_MONTH + DAY_OF_WEEK) instead of priority 1
(DAY_OF_MONTH). The WEEK_OF_MONTH field has the stale default value 1 from
`calendar.clear()`, not the value corresponding to day 27.

## DateFormatSymbols

### Bug 30070 -- DateFormatSymbols.getMonths() 13th element is null
**Status: FIXED**

The `getStringArray()` method at line 322 now initializes the array with
`Arrays.fill(data, "")`, ensuring all 13 elements are empty strings, never null.
The month array is properly sized to 13 elements.

### Bug 83968 -- Wrong parsing of strings in DateFormatSymbols
**Status: FIXED (commit 670a6669f)**

Locale resource strings contain trailing `\u00AE` (registered sign) delimiters.
`getStringArray()` uses `FIELD_SEP.split(localeData, limit)` to parse these.
The old code used `limit = size`, which caused the trailing delimiter to be
included as part of the last token (e.g., `"PM\u00AE"` instead of `"PM"`).
Commit `670a6669f` fixed this by changing the limit to `size + 1`, so the
trailing delimiter produces an extra empty-string element that falls outside
the `data.length` loop bounds and is harmlessly ignored.

## MailDateFormat (javax.mail.internet)

| Bug ID | Title |
|--------|-------|
| 28510 | StringIndexOutOfBoundsException in javax.mail.internet.MailDateFormat.parse |
| 28522 | javax.mail.internet.MailDateFormat: day-of-week should be optional |

(Ignore these 2)

## TimeZone

| Bug ID | Title |
|--------|-------|
| 16990 | [meta-bug] TimeZone Issues in libgcj/classpath |
| 23566 | Need to regenerate TimeZone data against tzdata2006a |
| 28550 | TimeZone.getDefault() probably incorrect and not equals(/etc/localtime) |

(Ignore these 3)

## java.util.Date

| Bug ID | Title |
|--------|-------|
| 22847 | java.util.Date toString() output doesn't match JDK |

(Ignore this one)

## DateFormat locale / parsing (composite issues)

### Bug 60259 -- getDateInstance() and getDateTimeInstance() hang locale without fallback
**Status: PARTIALLY FIXED**

The infinite loop for unsupported locales is fixed: `LocaleHelper.getFallbackLocale()`
(line 93) properly converges to `Locale.ROOT`, and `computeInstance()` handles
`Locale.ROOT` at line 472 by calling `computeDefault()`. However, a fall-through
bug remains in `computeDefault()` (DateFormat.java line 576): `case SHORT:` sets
the pattern but **falls through to `default: throw new IllegalArgumentException()`**
due to a missing `break`. This means `getDateInstance(SHORT)` with `Locale.ROOT`
fallback will throw `IllegalArgumentException` instead of returning a valid
format.

### Bug 64174 -- Failure to parse 2014-12-31-22-00-00
**Status: FIXED (verified by runtime test)**

Parsing December 31 dates in non-lenient mode now succeeds. Runtime test with
JamVM + clean master glibj.zip confirms `parse("2014-12-31-22-00-00")` with
pattern `"yyyy-MM-dd-HH-mm-ss"` in non-lenient mode returns a valid Date.
Same root cause as bug 29690, both fixed.

## Tangentially related

| Bug ID | Title |
|--------|-------|
| 25946 | Inconsistency between different getAvailableLocales() vs. getDefaultLocale() (mentions DateFormat.getAvailableLocales()) |
| 32486 | ResourceBundle/properties resolution (MissingResourceException for minNumberOfDaysInFirstWeek during Calendar init) |

(Ignore 25946)

### Bug 32486 -- ResourceBundle/properties resolution
**Status: FIXED**

The Calendar constructor no longer uses `ResourceBundle` for locale data. It now
loads `weeks.properties` via `Calendar.class.getResourceAsStream()` (line 498)
and looks up `minDays.<country>` with fallback to `minDays.DEFAULT`. This avoids
the ResourceBundle parent chain issue entirely and is not affected by stale
`LocaleInformation` classes on the classpath.

## Summary

| Bug ID | Title | Status |
|--------|-------|--------|
| 22785 | Calendar/getMinimalDaysInFirstWeek() for UK | STILL PRESENT |
| 25944 | Calendar DST crossing | STILL PRESENT |
| 29149 | GregorianCalendar DST (dup of 25944) | STILL PRESENT |
| 29690 | GregorianCalendar nonLeniencyCheck disagreement | FIXED |
| 31784 | GregorianCalendar.add(YEAR) broken | FIXED |
| 32443 | Calendar.set(HOUR_OF_DAY) changes date | STILL PRESENT |
| 28549 | SimpleDateFormat timezone output wrong | STILL PRESENT |
| 30359 | SimpleDateFormat parse exception | FIXED |
| 32278 | DateFormat.parse error (iCalendar) | FIXED |
| 39789 | SimpleDateFormat.parse incorrect Date | STILL PRESENT |
| 87590 | SimpleDateFormat weekday parse wrong | STILL PRESENT |
| 30070 | DateFormatSymbols.getMonths() 13th null | FIXED |
| 83968 | DateFormatSymbols trailing garbage chars | FIXED (670a6669f) |
| 60259 | getDateInstance() infinite loop | PARTIALLY FIXED |
| 64174 | Failure to parse Dec 31 (dup of 29690) | FIXED |
| 32486 | ResourceBundle/properties resolution | FIXED |
