#!/bin/bash
#
# Test script to verify javac -h generates equivalent JNI headers
# Usage: ./scripts/test-javac-h.sh
#

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
CLASSPATH_DIR="$(dirname "$SCRIPT_DIR")"
TEMP_DIR=$(mktemp -d)

#trap "rm -rf $TEMP_DIR" EXIT

echo "=== Testing javac -h equivalence ==="
echo "Temp dir: $TEMP_DIR"
echo

# List of Java files with native methods to test
NATIVE_CLASSES=(
    "vm/reference/java/lang/VMProcess.java"
    "vm/reference/java/lang/VMSystem.java"
    "vm/reference/java/lang/VMDouble.java"
    "vm/reference/java/lang/VMFloat.java"
)

cd "$CLASSPATH_DIR"

# Compile classes and generate headers with javac -h
echo "Generating headers with javac -h..."
javac -h "$TEMP_DIR" \
    -source 1.6 -target 1.6 -nowarn \
    -bootclasspath '' \
    -classpath "vm/reference:.:external/w3c_dom:external/sax:external/relaxngDatatype:external/jsr166:lib" \
    -d "$TEMP_DIR/classes" \
    "${NATIVE_CLASSES[@]}" 2>/dev/null

echo
echo "=== Comparing generated headers with existing ==="
echo

PASS=0
FAIL=0

for header in "$TEMP_DIR"/*.h; do
    basename=$(basename "$header")
    existing="include/$basename"

    if [ ! -f "$existing" ]; then
        echo "SKIP: $basename (no existing header)"
        continue
    fi

    # Extract native method declarations (JNIEXPORT lines)
    grep "^JNIEXPORT" "$header" | sort > "$TEMP_DIR/new_methods.txt"
    grep "^JNIEXPORT" "$existing" | sort > "$TEMP_DIR/old_methods.txt"

    # Extract #define constants
    # Get class name from header (e.g., java_lang_VMProcess from java_lang_VMProcess.h)
    classname="${basename%.h}"

    # From existing header, extract only main class defines (not inner class ones)
    # Main class defines: #define java_lang_VMProcess_INITIAL (ALL_CAPS constant)
    # Inner class defines: #define java_lang_VMProcess_ProcessThread_X (CamelCase inner class name)
    # Pattern: classname followed by underscore and ALL_CAPS only (no lowercase = not an inner class)
    grep "^#define ${classname}_[A-Z][A-Z0-9_]* " "$existing" 2>/dev/null | sort > "$TEMP_DIR/old_defines.txt" || true
    grep "^#define" "$header" | sort > "$TEMP_DIR/new_defines.txt"

    methods_ok=true
    defines_ok=true

    if ! diff -q "$TEMP_DIR/new_methods.txt" "$TEMP_DIR/old_methods.txt" > /dev/null 2>&1; then
        methods_ok=false
    fi

    # Check that all main-class defines from existing header exist in generated header
    # (this verifies javac -h produces all necessary defines)
    while IFS= read -r line; do
        if ! grep -qF "$line" "$TEMP_DIR/new_defines.txt"; then
            defines_ok=false
            break
        fi
    done < "$TEMP_DIR/old_defines.txt"

    if $methods_ok && $defines_ok; then
        echo "PASS: $basename - native methods and #defines match"
        PASS=$((PASS + 1))
    else
        echo "FAIL: $basename"
        if ! $methods_ok; then
            echo "  Native method signatures differ:"
            diff "$TEMP_DIR/old_methods.txt" "$TEMP_DIR/new_methods.txt" | sed 's/^/    /' || true
        fi
        if ! $defines_ok; then
            echo "  #define constants missing from javac -h output:"
            echo "    Expected (from existing header):"
            cat "$TEMP_DIR/old_defines.txt" | sed 's/^/      /'
            echo "    Generated (by javac -h):"
            cat "$TEMP_DIR/new_defines.txt" | sed 's/^/      /'
        fi
        FAIL=$((FAIL + 1))
    fi
done

echo
echo "=== Summary ==="
echo "Passed: $PASS"
echo "Failed: $FAIL"

if [ $FAIL -eq 0 ]; then
    echo
    echo "SUCCESS: javac -h generates equivalent headers"
    exit 0
else
    echo
    echo "FAILURE: Some headers differ"
    exit 1
fi
