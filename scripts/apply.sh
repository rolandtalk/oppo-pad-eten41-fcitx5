#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
TARGET="${1:-}"
ANDROID_REVISION="048f581c652367567b8ee5c28c5163b805288895"
CHEWING_REVISION="07eddb16961b18765e67cec538708b6964baa57c"
CHEWING_DIR="plugin/chewing/src/main/cpp/fcitx5-chewing"

if [[ -z "$TARGET" ]]; then
    echo "Usage: $0 /path/to/fcitx5-android" >&2
    exit 2
fi

TARGET="$(cd "$TARGET" && pwd)"
if [[ "$(git -C "$TARGET" rev-parse HEAD)" != "$ANDROID_REVISION" ]]; then
    echo "Expected fcitx5-android revision $ANDROID_REVISION" >&2
    exit 1
fi

git -C "$TARGET" submodule update --init "$CHEWING_DIR"
if [[ "$(git -C "$TARGET/$CHEWING_DIR" rev-parse HEAD)" != "$CHEWING_REVISION" ]]; then
    echo "Expected fcitx5-chewing revision $CHEWING_REVISION" >&2
    exit 1
fi

git -C "$TARGET" apply "$ROOT_DIR/patches/fcitx5-android.patch"
git -C "$TARGET/$CHEWING_DIR" apply "$ROOT_DIR/patches/fcitx5-chewing.patch"
cp -R "$ROOT_DIR/overlay/." "$TARGET/"

echo "Eten 41 keyboard and Chewing TSI associations applied to $TARGET"
