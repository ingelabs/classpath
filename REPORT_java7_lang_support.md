# Java 7 Language Support in GNU Classpath

## Scope

This report covers what is required in the GNU Classpath class library to support
Java 7 language-level features — that is, the features introduced by Project Coin
(JSR 334) and JSR 292. This is distinct from Java 7 API additions (NIO.2, Fork/Join,
etc.), which are not in scope here.

---

## Java 7 Language Features and Library Requirements

| Feature | Library requirement | Status |
|---|---|---|
| Diamond operator `<>` | None — erased at compile time | N/A |
| String in `switch` | None — compiled to `hashCode()`/`equals()` chains | N/A |
| Underscore in numeric literals | None — lexer feature | N/A |
| Binary literals (`0b...`) | None — lexer feature | N/A |
| Multi-catch (`catch (A \| B e)`) | None — pure compiler desugaring | N/A |
| **Try-with-resources** | `java.lang.AutoCloseable` must exist; `java.io.Closeable` must extend it | Already done |
| **Try-with-resources (suppression)** | `Throwable.addSuppressed()` / `Throwable.getSuppressed()` | **Implemented** |
| **Simplified Varargs (JSR 334)** | `java.lang.SafeVarargs` annotation type must exist | **Implemented** |
| `invokedynamic` (JSR 292) | `java.lang.invoke.*` package (needed for dynamic languages targeting JVM 7) | Not in scope / not done |

---

## Pre-existing State

- **`java.lang.AutoCloseable`** — already present (`java/lang/AutoCloseable.java`).
- **`java.io.Closeable`** — already extends `AutoCloseable` (`java/io/Closeable.java`).
- **`java.lang.Throwable`** — had no suppressed exception support at all.
- **`java.lang.SafeVarargs`** — did not exist.

---

## What Was Implemented

### `java/lang/Throwable.java`

The following changes were made to add suppressed exception support, which the
compiler relies on when desugaring try-with-resources blocks. When both the body
and the `close()` call throw, the compiler emits calls to `addSuppressed()` to
attach the secondary exception to the primary one instead of silently discarding it.

#### New fields

```java
// Holds suppressed exceptions; null when suppression is disabled.
private List<Throwable> suppressedExceptions = new ArrayList<Throwable>();

// Controls whether fillInStackTrace() runs (used by the new constructor).
private transient boolean writableStackTrace = true;

// Reusable empty array returned by getSuppressed() when there is nothing to return.
private static final Throwable[] EMPTY_THROWABLE_ARRAY = new Throwable[0];
```

#### New constructor (Java 7)

```java
protected Throwable(String message, Throwable cause,
                    boolean enableSuppression, boolean writableStackTrace)
```

Allows subclasses to disable suppression and/or skip filling in the stack trace
(useful for high-frequency exceptions where stack traces are too expensive).

#### `addSuppressed(Throwable exception)`

- `final` — cannot be overridden.
- Throws `NullPointerException` if `exception` is null.
- Throws `IllegalArgumentException` if `exception == this` (self-suppression).
- No-op if suppression was disabled via the constructor.

#### `getSuppressed()`

- `final` — cannot be overridden.
- Returns a snapshot of the suppressed list as a `Throwable[]`.
- Returns `EMPTY_THROWABLE_ARRAY` if the list is empty or suppression is disabled.

#### `stackTraceString()` update

Suppressed exceptions are now printed between the main stack trace and the
"Caused by:" section, matching the format used by the JDK:

```
SomeException: message
    at ...
    Suppressed: AnotherException: message
        at ...
Caused by: ...
```

### `java/lang/SafeVarargs.java`

New annotation type for "Simplified Varargs Method Invocation" (JSR 334).

The declaration-site "Possible heap pollution" warning (new in `-source 1.7`) is
suppressed by placing `@SafeVarargs` on a method or constructor. The annotation
type must exist in the class library for user code using it to compile against
Classpath.

Key notes:
- Classpath's own varargs methods do **not** need to be annotated — that would
  only reduce warnings when recompiling Classpath itself, not for downstream users.
- The call-site unchecked warnings for generic varargs are present in both
  `-source 1.6` and `-source 1.7`; only the declaration-site warning is new in 1.7.
- `@Retention(RUNTIME)` so reflection and tooling can see it; `@Target` restricted
  to `CONSTRUCTOR` and `METHOD`.

---

## What Remains for Full Java 7 Language Support

- `java.lang.invoke.*` — needed to run code that uses `invokedynamic` (e.g. dynamic
  languages, and potentially future use by javac for lambda support in Java 8).
  This is a large package and is a separate work item.

With the changes above, all Project Coin language features are fully supported at
the library level. All remaining features (diamond, string switch, multi-catch,
numeric literal syntax) are purely compiler-level and require no library changes.
