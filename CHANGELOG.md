# Changelog

## v0.2.0 - 2026-09-25

### Candidate Words

- Show Chewing homophone candidates immediately after a Zhuyin syllable is
  completed.
- Allow the next Zhuyin key to close the transient candidate list and continue
  typing the next character without an extra candidate tap.

### Association Words

- Generate an offline next-character association index from Chewing's official
  `tsi.csv` phrase dictionary.
- Rank associations by phrase frequency and use up to four preceding characters
  as context.
- Continue association suggestions after selecting a related character.

### Punctuation Input

- Keep normal taps mapped to Zhuyin on the Eten punctuation keys.
- Add long-press and downward-swipe input for the printed symbols:
  `ㄓ` -> `,`, `ㄔ` -> `.`, `ㄕ` -> `/`, `ㄗ` -> `;`, and `ㄘ` -> `'`.

### Distribution

- Rebuild the matching arm64-v8a debug-signed main app and Chewing plugin for
  OPPO Pad devices.
