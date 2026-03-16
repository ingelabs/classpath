#!/bin/sh

set -eu

help() {
  cat <<EOF
Usage: $0 [OPTION]... <old-ref>

List glibj Java source files changed between <old-ref> and HEAD.

Options:
  -e, --exclude FILE
                exclude sources listed in FILE
  -h, --help    display this help and exit
EOF
  exit 0
}

EXCLUDE_PATH=""

while [ "$#" -gt 0 ]; do
  case "$1" in
    -e|--exclude) EXCLUDE_PATH="$2"; shift 2 ;;
    -h|--help) help ;;
    -*) echo "$0: unknown option: $1" >&2; exit 1 ;;
    *) break ;;
  esac
done

if [ "$#" -ne 1 ]; then
  echo "$0: expected exactly one argument" >&2
  exit 1
fi
OLD_REF=$1
NEW_REF=HEAD

if [ -n "$EXCLUDE_PATH" ] && [ ! -e "$EXCLUDE_PATH" ]; then
  echo "error: exclude file not found: $EXCLUDE_PATH" >&2
  exit 1
fi

is_excluded() {
  src=$1

  while IFS= read -r exclude; do
    exclude=$(printf '%s\n' "$exclude" | sed 's/^[[:space:]]*//; s/[[:space:]]*$//')

    case "$exclude" in
      ''|\#*) continue ;;
      */) exclude=${exclude%/} ;;
    esac

    case "$exclude" in
      *.java)
        [ "$src" = "$exclude" ] && return 0
        ;;
      *)
        case "$src" in
          "$exclude"/*) return 0 ;;
        esac
        ;;
    esac
  done < "$EXCLUDE_PATH"

  return 1
}

is_glibj_source() {
  # Source dirs mirror what gen-classlist.sh scans
  case "$1" in
    java/*|javax/*|gnu/*|org/*|sun/*|external/*|vm/reference/*) return 0 ;;
    *) return 1 ;;
  esac
}

CHANGED_SOURCES=$(
  git diff --name-only --diff-filter=ACMR "$OLD_REF" "$NEW_REF" -- '*.java' |
  while IFS= read -r src; do
    is_glibj_source "$src" || continue

    if [ -n "$EXCLUDE_PATH" ] && is_excluded "$src"; then
      continue
    fi

    printf '%s\n' "$src"
  done
)

if [ -z "$CHANGED_SOURCES" ]; then
  echo "No modified glibj Java sources found between ${OLD_REF} and ${NEW_REF}."
  exit 0
fi

printf '%s\n' "$CHANGED_SOURCES"
