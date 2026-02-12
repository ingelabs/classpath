# Darwin/aarch64 Headless Bring-up Report

Date: 2026-02-12

## Objective

Analyze what is needed to support `darwin/aarch64`, then implement the scoped preliminary changes for a headless target (no GTK/Qt/AWT peer work in this changeset).

## Agreed Scope

Based on follow-up decisions:

1. Do **not** modify `config.guess` / `config.sub` in this changeset.
2. Add Darwin JNI header support and normalize `configure.ac` lookup for Darwin/aarch64.
3. Keep existing fallback behavior (`x86-linux-gnu`) unchanged.
4. Do **not** change peer defaults (GTK/GConf/etc.) in this changeset.
5. Defer kqueue/NIO correctness fixes for a later patch.

## Changes Implemented

### 1) `configure.ac` updates

- Added CPU alias normalization:
  - `arm64 -> aarch64`
  - Location: `configure.ac`
- Updated JNI md header lookup logic to use normalized local variables:
  - `classpath_jni_md_cpu`
  - `classpath_jni_md_os`
- Added Darwin OS normalization for JNI md header selection:
  - `darwin* -> darwin`
- Kept existing fallback semantics unchanged:
  - If no matching header is found, fallback remains `x86-linux-gnu`.

### 2) New JNI md header for Darwin ARM64

- Added file:
  - `include/jni_md-aarch64-darwin.h`
- Contents intentionally match existing generic JNI md definitions used in-tree, aligned with current header style.

## Validation Performed

### Autotools regeneration

Command:

```bash
./autogen.sh
```

Result:

- Completed successfully.
- Regenerated `configure` and related autotools artifacts.

### Configure probe for Darwin ARM64

Command used:

```bash
touch /tmp/glibj.zip
./configure \
  --host=aarch64-apple-darwin \
  --disable-gtk-peer \
  --disable-gconf-peer \
  --disable-plugin \
  --disable-qt-peer \
  --disable-gstreamer-peer \
  --disable-xmlj \
  --disable-alsa \
  --disable-dssi \
  --disable-examples \
  --disable-tools \
  --enable-default-preferences-peer=file \
  --enable-default-toolkit=gnu.java.awt.peer.headless.HeadlessToolkit \
  --with-glibj-zip=/tmp/glibj.zip
```

Key output:

- `checking host system type... aarch64-apple-darwin`
- `checking jni_md.h support... yes`
- `config.status: linking include/jni_md-aarch64-darwin.h to include/jni_md.h`

Post-check:

- `include/jni_md.h` correctly linked to `include/jni_md-aarch64-darwin.h`.

## Files Changed (intentional)

- `configure.ac`
- `include/jni_md-aarch64-darwin.h`

## Not Included In This Changeset

- No modifications to `config.guess` / `config.sub`.
- No peer-default policy changes by platform.
- No AWT/JAWT/Cocoa work.
- No kqueue timeout/type fixes.
- No full Darwin native toolchain compilation.

## Limitations

The configure probe validates selection logic only. It does **not** prove end-to-end Darwin/aarch64 runtime compatibility, since this was run in a Linux workspace (not with an actual Darwin linker/SDK/toolchain).

