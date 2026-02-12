# Report: Add darwin/aarch64 (Apple Silicon) support

## Summary

Added explicit support for building GNU Classpath's core library on darwin/aarch64 (Apple Silicon Macs). Previously, configuring for this target would silently fall back to the x86-linux-gnu JNI header with a warning. The native C code was already portable (POSIX), and several Darwin-specific concerns were already handled upstream: `environ` via `crt_externs.h`, `SO_NOSIGPIPE` for socket send, kqueue-based NIO selector, aarch64 endianness in fdlibm's `ieeefp.h`, and libtool dylib output.

The only actual gaps were: (1) a missing platform-specific JNI machine-dependent header, and (2) `configure.ac` not normalizing the versioned darwin OS string that `config.guess` produces.

## Existing darwin/aarch64 support (no changes needed)

- **`native/jni/classpath/jcl.h`** -- `crt_externs.h` / `_NSGetEnviron()` for Darwin's `environ`
- **`native/jni/native-lib/cpnet.c`** -- `SO_NOSIGPIPE` fallback when `MSG_NOSIGNAL` is unavailable
- **`native/jni/java-nio/gnu_java_nio_KqueueSelectorImpl.c`** -- kqueue/kevent selector (Darwin alternative to Linux's epoll)
- **`native/fdlibm/ieeefp.h`** -- aarch64 little-endian detection via `__AARCH64EL__`
- **`configure.ac`** -- `-module` flag disabled on darwin for libtool; `sys/event.h` / `kqueue` / `kevent` checked
- **Process spawning (`cpproc.c`)** -- pure POSIX fork/exec/waitpid, no Linux-specific calls
- **Shared library naming** -- handled automatically by libtool (`LT_INIT`)

## Files created

### 1. `include/jni_md-aarch64-darwin.h`
JNI machine-dependent type definitions for darwin/aarch64. Identical contents to `jni_md-x86-linux-gnu.h` (both LP64 with the same type widths). Having a properly named file eliminates the configure-time fallback warning and documents explicit platform support.

## Files modified

### 1. `configure.ac`
Added a `target_os` normalization block after the existing x86 CPU normalization. `config.guess` on macOS reports versioned OS strings like `darwin23.0.0`, but the `jni_md` header lookup at line 825 uses `${target_os}` directly. The new block strips the version:

```m4
case "$target_os" in
     darwin*) target_os=darwin ;;
esac
```

This ensures the lookup resolves to `include/jni_md-aarch64-darwin.h`. The existing `host_os` checks (for `-module` flag, `-Werror` default) are unaffected since they use `host_os`, not `target_os`.

## Features that must be disabled on Darwin

These are Linux-specific optional peers and should be disabled at configure time:

- GTK peers (`--disable-gtk-peer`) -- requires X11
- GConf preferences (`--disable-gconf-peer`) -- GNOME-specific
- ALSA MIDI (`--disable-alsa`) -- Linux audio
- GStreamer audio (`--disable-gstreamer-peer`) -- Linux audio
- Browser plugin (`--disable-plugin`) -- Mozilla/Firefox-specific

Recommended configure invocation:

```sh
./configure --host=aarch64-apple-darwin \
    --disable-gtk-peer --disable-gconf-peer \
    --disable-alsa --disable-gstreamer-peer \
    --disable-plugin \
    --enable-default-preferences-peer=file
```

## Known Darwin quirk (pre-existing)

`native/jni/java-nio/java_nio_MappedByteBufferImpl.c:165-166` documents a known `mincore()` bug on Darwin where single-page mapped regions are incorrectly reported as not loaded. This is pre-existing and not addressed here.

## Verification

- Confirmed `config.guess` produces `aarch64-apple-darwin*` on Apple Silicon (no arm64 normalization needed -- config.guess already handles this).
- Header lookup: `target_cpu=aarch64`, `target_os=darwin` -> `include/jni_md-aarch64-darwin.h` exists.
