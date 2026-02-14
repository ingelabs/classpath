# GNU Classpath

GNU Classpath is a set of core class libraries for use with Java Virtual Machines, providing roughly Java 1.6 (Java SE 6) API compatibility.

## Why GNU Classpath

GNU Classpath provides a lightweight alternative to OpenJDK for environments where a full JDK is unnecessary. Combined with a compact VM such as [JamVM](https://github.com/ingelabs/jamvm), it is well suited for embedded and resource-constrained devices, offering:

- Smaller flash and RAM footprint
- Lower startup overhead
- Minimal impact on runtime performance for typical workloads

This makes it a practical choice when deploying Java applications on hardware where the size and startup cost of a full OpenJDK installation is prohibitive.

## Supported platforms

The primary targets are Linux embedded systems. The following architectures are supported:

- Linux / 32-bit ARM
- Linux / 64-bit ARM (aarch64)
- Linux / x86_64

macOS with Apple Silicon is also supported for development (not as a target).

Other platforms supported by the build system may work but are not actively tested.

## Building GNU Classpath

### Prerequisites

A Java 8 JDK is required to compile the class library.

For Linux, Temurin or Zulu are good choices. For macOS, Zulu is recommended (Temurin is not available for Apple Silicon).

Additional prerequisites for a typical headless configuration:

**Linux (Debian/Ubuntu):**

```sh
sudo apt-get install build-essential autoconf automake libtool gettext texinfo
```

**macOS:**

```sh
brew install autoconf automake libtool gettext texinfo
```

You may need to link the gettext m4 macros into the aclocal directory (see [Homebrew#53192](https://github.com/Homebrew/homebrew-core/issues/53192)):

```sh
ln -sf "$(brew --prefix gettext)/share/gettext/m4/"*.m4 "$(aclocal --print-ac-dir)/"
```

### Build

```sh
autoreconf -iv
./configure \
  --disable-gtk-peer \
  --disable-gconf-peer \
  --enable-default-preferences-peer=file \
  --disable-examples \
  --disable-tools \
  --disable-gjdoc
make
sudo make install
```

### Cross-compilation

When building for embedded targets, a cross-compilation toolchain is needed.

For building complete embedded Linux images, consider using tools such as [Buildroot](https://buildroot.org/) or [ptxdist](https://www.ptxdist.org/).

## Building applications

To compile a Java application against GNU Classpath, use `-bootclasspath` to point to the Classpath library classes, for example:

```sh
javac -source 1.6 -target 1.6 -bootclasspath /usr/local/classpath/share/classpath/glibj.zip HelloWorld.java
```

This ensures the compiler only allows APIs provided by GNU Classpath. Any use of unsupported APIs will result in a compile-time error.

## Running applications

To run an application with [JamVM](https://github.com/ingelabs/jamvm):

```sh
jamvm HelloWorld
```

JamVM automatically locates the GNU Classpath library classes from its installation prefix.

## History

This project is a fork of the original GNU Classpath project, which is no longer actively maintained (last release: 0.99, March 2012).

The original project site can be found here: https://www.gnu.org/software/classpath/

This fork continues development with a focus on embedded Linux targets.
