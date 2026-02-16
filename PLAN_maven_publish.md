# Publishing GNU Classpath to Maven Central

## Maven coordinates

```
com.ingelabs.classpath:classpath:0.99.1
```

- **Group ID**: `com.ingelabs.classpath` — scoped under `com.ingelabs` (verified domain), with `classpath` as project namespace. Follows the convention used by `com.google.guava`, `org.apache.commons`, etc.
- **Artifact ID**: `classpath` — no suffix (`-core`, `-api`). If native artifacts are added later, they get their own artifact IDs alongside this one.
- **Version**: `0.99.1` — continues the version lineage from the last GNU Classpath release (0.99, March 2012). No fork marker in the version; the group ID already distinguishes the publisher.

## What is published

A single, platform-independent artifact containing the compiled Java class library (`glibj.zip` repackaged as a JAR). No native libraries.

The primary use case is as a bootclasspath for compiling applications:

```sh
javac -source 1.6 -target 1.6 -bootclasspath /path/to/classpath-0.99.1.jar HelloWorld.java
```

Or as a Maven/Gradle dependency with `provided` scope (compile-time only, not bundled).

### Published artifacts

| File | Content |
|---|---|
| `classpath-0.99.1.jar` | Compiled classes (`glibj.zip`) |
| `classpath-0.99.1-sources.jar` | Java source files |
| `classpath-0.99.1-javadoc.jar` | Empty placeholder (Maven Central requirement) |
| `classpath-0.99.1.pom` | POM with project metadata |

## Build configuration

The configure options used when building GNU Classpath do **not** affect the contents of `glibj.zip`. The Java bytecode is identical regardless of which native peers are enabled or disabled. Specifically:

| Flag | Affects glibj.zip? | What it affects |
|---|---|---|
| `--disable-gtk-peer` | No | Native `.so` only |
| `--disable-gconf-peer` | No | Native `.so` only |
| `--enable-default-preferences-peer=file` | No | Runtime service config |
| `--disable-examples` | No | Separate example files |
| `--disable-tools` | No | Separate tools build |
| `--disable-gjdoc` | No | Documentation tool |

Therefore any headless or headful build of Classpath produces the same Maven artifact.

## Gradle setup

Publishing is handled by Gradle via the `maven-publish` and `signing` plugins. Gradle does not compile the Java sources — it packages and publishes the `glibj.zip` produced by the autotools build (`make`).

Files added to the repository:

- `build.gradle` — publishing configuration
- `settings.gradle` — project name
- `gradlew`, `gradlew.bat`, `gradle/wrapper/` — Gradle wrapper (no global install needed)

## Signing

Maven Central requires all artifacts to be GPG-signed.

- GPG key: `8D1381E9E8855C7C`
- Published to: `keyserver.ubuntu.com`
- Signing is only required when publishing to Sonatype (`./gradlew publish`), not for local testing (`./gradlew publishToMavenLocal`).

## Credentials

Stored in `~/.gradle/gradle.properties` (not in the repo):

```properties
sonatypeUsername=<token>
sonatypePassword=<token>
signing.gnupg.keyName=8D1381E9E8855C7C
signing.gnupg.passphrase=<passphrase>
```

## Publishing workflow

1. Build Classpath: `make` (produces `lib/glibj.zip`)
2. Test locally: `./gradlew publishToMavenLocal` (publishes to `~/.m2/repository/`)
3. Publish to staging: `./gradlew publish` (uploads to Sonatype)
4. Release: log into https://central.sonatype.com/, verify staged artifacts, release

## Future considerations

- **Native artifacts**: If needed, publish separate classified artifacts per platform (e.g. `classpath-native:linux-aarch64`), following the pattern used by Netty and LWJGL.
- **Javadoc JAR**: Currently empty. If Maven Central rejects it, generate actual javadoc via `gjdoc` or standard `javadoc`.
- **Version 1.0.0**: Consider for a future milestone when the fork has made significant changes beyond the original 0.99 release.
