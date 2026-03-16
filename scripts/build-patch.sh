#!/bin/sh

set -eu

help() {
  cat <<EOF
Usage: $0 [OPTION]... [SOURCES-FILE]

Build a glibj patch ZIP archive from a list of changed Java source files.
Reads source paths from SOURCES-FILE, or from standard input if omitted.

Options:
  -o, --output FILE
                write patch archive to FILE (default: lib/glibj-patch.zip)
  -h, --help    display this help and exit
EOF
  exit 0
}

OUTPUT_PATH=""

while [ "$#" -gt 0 ]; do
  case "$1" in
    -o|--output) OUTPUT_PATH="$2"; shift 2 ;;
    -h|--help) help ;;
    -*) echo "$0: unknown option: $1" >&2; exit 1 ;;
    *) break ;;
  esac
done

if [ "$#" -gt 1 ]; then
  echo "$0: too many arguments" >&2
  exit 1
fi
SOURCES_FILE="${1:-}"

REPO_ROOT="$(git rev-parse --show-toplevel)"

if [ -z "$OUTPUT_PATH" ]; then
  OUTPUT_PATH="${REPO_ROOT}/lib/glibj-patch.zip"
elif [ "${OUTPUT_PATH#/}" = "$OUTPUT_PATH" ]; then
  OUTPUT_PATH="$PWD/$OUTPUT_PATH"
fi

if [ -n "$SOURCES_FILE" ]; then
  CHANGED_SOURCES=$(cat "$SOURCES_FILE")
else
  if [ -t 0 ]; then
    # true if stdin is a terminal (i.e. no pipe or redirect)
    echo "$0: expected list of .java paths on stdin or as a file argument" >&2
    exit 1
  fi

  CHANGED_SOURCES=$(cat)
fi

if [ -z "$CHANGED_SOURCES" ]; then
  echo "No modified Java sources to build."
  exit 0
fi

source_base() {
  src=$1

  awk -v root="$REPO_ROOT" -v src="$src" '
    {
      full = $2 "/" $3
      prefix = root "/"

      if (index(full, prefix) == 1) {
        full = substr(full, length(prefix) + 1)
      }

      if (full == src) {
        sub(/\.java$/, "", $3)
        print $3
        exit
      }
    }
  ' "$REPO_ROOT/lib/classes.1"
}

PATCH_LIST="$(mktemp "${TMPDIR:-/tmp}/build-patch-archive.XXXXXX")"

cleanup() {
  rm -f "$PATCH_LIST"
}
trap cleanup EXIT
trap 'exit 1' HUP INT QUIT TERM

printf '%s\n' "$CHANGED_SOURCES" |
while IFS= read -r src; do
  base=$(source_base "$src")

  [ -n "$base" ] || continue

  if [ -f "${REPO_ROOT}/lib/${base}.class" ]; then
    printf '%s\n' "${base}.class"
  fi

  for inner in "${REPO_ROOT}/lib/${base}"\$*.class; do
    [ -f "$inner" ] || continue
    printf '%s\n' "${inner#${REPO_ROOT}/lib/}"
  done
done > "$PATCH_LIST"

if [ ! -s "$PATCH_LIST" ]; then
  echo "$0: no compiled class files found for modified Java sources" >&2
  exit 1
fi

rm -f "$OUTPUT_PATH"

(
  cd "${REPO_ROOT}/lib"
  jar cf "$OUTPUT_PATH" @"$PATCH_LIST"
)

echo "Patch archive created: $OUTPUT_PATH"
