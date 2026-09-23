#!/usr/bin/env python3
"""Generate a compact next-character association index from Chewing tsi.csv."""

import argparse
import csv
from collections import defaultdict
from pathlib import Path


def is_han(character: str) -> bool:
    codepoint = ord(character)
    return (
        0x3400 <= codepoint <= 0x4DBF
        or 0x4E00 <= codepoint <= 0x9FFF
        or 0xF900 <= codepoint <= 0xFAFF
        or 0x20000 <= codepoint <= 0x323AF
    )


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser()
    parser.add_argument("source", type=Path, help="Path to Chewing tsi.csv")
    parser.add_argument("output", type=Path, help="Generated TSV output")
    parser.add_argument("--max-context", type=int, default=4)
    parser.add_argument("--max-candidates", type=int, default=8)
    parser.add_argument("--source-revision", default="unknown")
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    scores: dict[str, dict[str, int]] = defaultdict(lambda: defaultdict(int))

    with args.source.open(encoding="utf-8", newline="") as source:
        for row in csv.reader(source):
            if len(row) < 2 or not row[0] or row[0].startswith("#"):
                continue
            phrase = row[0]
            if len(phrase) < 2 or not all(is_han(character) for character in phrase):
                continue
            try:
                frequency = max(1, int(row[1]))
            except ValueError:
                continue

            for index in range(1, len(phrase)):
                candidate = phrase[index]
                for length in range(1, min(args.max_context, index) + 1):
                    context = phrase[index - length : index]
                    scores[context][candidate] += frequency

    args.output.parent.mkdir(parents=True, exist_ok=True)
    with args.output.open("w", encoding="utf-8", newline="\n") as output:
        output.write("# Generated from Chewing tsi.csv\n")
        output.write(f"# Source revision: {args.source_revision}\n")
        output.write("# Dictionary license: LGPL-2.1-or-later\n")
        for context in sorted(scores):
            candidates = sorted(
                scores[context].items(), key=lambda item: (-item[1], item[0])
            )[: args.max_candidates]
            output.write(context)
            for candidate, _score in candidates:
                output.write(f"\t{candidate}")
            output.write("\n")


if __name__ == "__main__":
    main()
