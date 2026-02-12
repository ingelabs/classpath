#!/bin/bash
#
# javah-wrapper.sh - Emulates javah using javac -h for Java 9+
#
# This script provides a javah-compatible interface using javac -h,
# allowing JNI header generation without the deprecated javah tool.
#
# Usage: javah-wrapper.sh -src <source.java> -o <output.h> [-classpath <cp>] [classname]
#   -src <file>     Source file to compile (required)
#   -o <file>       Output file name
#   -d <dir>        Output directory (alternative to -o)
#   -classpath <path>   Classpath for compilation
#   -bootclasspath <path>  Same as -classpath
#   -jni            Ignored (always JNI)
#
# Any class name arguments are ignored since javac -h works with source files.
#

set -e

# Parse arguments
OUTPUT=""
OUTPUT_DIR=""
SOURCE_FILE=""
CLASSPATH=""

while [ $# -gt 0 ]; do
    case "$1" in
        -src)
            SOURCE_FILE="$2"
            shift 2
            ;;
        -o)
            OUTPUT="$2"
            shift 2
            ;;
        -d)
            OUTPUT_DIR="$2"
            shift 2
            ;;
        -jni)
            # Ignored - javac -h always generates JNI headers
            shift
            ;;
        -classpath|-cp|-bootclasspath)
            CLASSPATH="$2"
            shift 2
            ;;
        -*)
            echo "Error: Unknown option: $1" >&2
            exit 1
            ;;
        *)
            # Class names are silently ignored
            shift
            ;;
    esac
done

if [ -z "$SOURCE_FILE" ]; then
    echo "Error: No source file specified (use -src)" >&2
    exit 1
fi

if [ ! -f "$SOURCE_FILE" ]; then
    echo "Error: Source file not found: $SOURCE_FILE" >&2
    exit 1
fi

# Create temp directory for compilation
TEMP_DIR=$(mktemp -d)
trap "rm -rf $TEMP_DIR" EXIT

# Build javac command
# Use -source/-target 1.6 to match build system and avoid module conflicts
# Use implicit:none to avoid compiling dependencies
JAVAC_ARGS="-h $TEMP_DIR -d $TEMP_DIR/classes -source 1.6 -target 1.6 -implicit:none -nowarn"

if [ -n "$CLASSPATH" ]; then
    # Use the provided classpath, but override bootclasspath to avoid JDK's java.*
    JAVAC_ARGS="$JAVAC_ARGS -bootclasspath '' -classpath $CLASSPATH"
else
    JAVAC_ARGS="$JAVAC_ARGS -bootclasspath ''"
fi

# Generate header using javac -h
eval javac $JAVAC_ARGS "$SOURCE_FILE"

# Find the generated header file (there should be exactly one)
GENERATED_HEADER=$(find "$TEMP_DIR" -maxdepth 1 -name "*.h" -type f)

if [ -z "$GENERATED_HEADER" ]; then
    echo "Error: No header file was generated" >&2
    echo "Source file: $SOURCE_FILE" >&2
    exit 1
fi

# Copy to output location
if [ -n "$OUTPUT" ]; then
    cp "$GENERATED_HEADER" "$OUTPUT"
elif [ -n "$OUTPUT_DIR" ]; then
    cp "$GENERATED_HEADER" "$OUTPUT_DIR/"
else
    # Default: output to current directory
    cp "$GENERATED_HEADER" "./"
fi
