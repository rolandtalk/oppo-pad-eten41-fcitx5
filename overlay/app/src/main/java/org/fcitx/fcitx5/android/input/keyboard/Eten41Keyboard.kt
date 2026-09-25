/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * SPDX-FileCopyrightText: Copyright 2026 Fcitx5 for Android Contributors
 */
package org.fcitx.fcitx5.android.input.keyboard

import android.annotation.SuppressLint
import org.fcitx.fcitx5.android.R
import org.fcitx.fcitx5.android.core.InputMethodEntry
import org.fcitx.fcitx5.android.data.theme.Theme
import splitties.views.imageResource

/** Windows-style Eten 41-key layout for the Chewing input method. */
@SuppressLint("ViewConstructor")
class Eten41Keyboard(
    context: android.content.Context,
    theme: Theme
) : BaseKeyboard(context, theme, Layout) {

    companion object {
        const val Name = "Eten41"

        private fun key(
            pcKey: String,
            bopomofo: String = "",
            literalGesture: String? = null
        ) = KeyDef(
            KeyDef.Appearance.AltText(
                displayText = pcKey,
                altText = bopomofo,
                textSize = 20f,
                percentWidth = when (pcKey) {
                    "[", "]", "\\" -> 0.075f
                    else -> 0.077f
                }
            ),
            buildSet {
                add(KeyDef.Behavior.Press(KeyAction.FcitxKeyAction(pcKey)))
                literalGesture?.let {
                    add(KeyDef.Behavior.LongPress(KeyAction.CommitAction(it)))
                    add(KeyDef.Behavior.Swipe(KeyAction.CommitAction(it)))
                }
            },
            arrayOf(KeyDef.Popup.AltPreview(pcKey, bopomofo))
        )

        val Layout: List<List<KeyDef>> = listOf(
            listOf(
                key("1", "˙"), key("2", "ˊ"), key("3", "ˇ"), key("4", "ˋ"),
                key("5"), key("6"), key("7", "ㄑ"), key("8", "ㄢ"),
                key("9", "ㄣ"), key("0", "ㄤ"), key("-", "ㄥ"), key("=", "ㄦ"),
                BackspaceKey(percentWidth = 0.076f)
            ),
            listOf(
                key("q", "ㄟ"), key("w", "ㄝ"), key("e", "ㄧ"), key("r", "ㄜ"),
                key("t", "ㄊ"), key("y", "ㄡ"), key("u", "ㄩ"), key("i", "ㄞ"),
                key("o", "ㄛ"), key("p", "ㄆ"), key("["), key("]"), key("\\")
            ),
            listOf(
                key("a", "ㄚ"), key("s", "ㄙ"), key("d", "ㄉ"), key("f", "ㄈ"),
                key("g", "ㄐ"), key("h", "ㄏ"), key("j", "ㄖ"), key("k", "ㄎ"),
                key("l", "ㄌ"), key(";", "ㄗ"), key("'", "ㄘ")
            ),
            listOf(
                key("z", "ㄠ"), key("x", "ㄨ"), key("c", "ㄒ"), key("v", "ㄍ"),
                key("b", "ㄅ"), key("n", "ㄋ"), key("m", "ㄇ"), key(",", "ㄓ", ","),
                key(".", "ㄔ"), key("/", "ㄕ")
            ),
            listOf(
                LayoutSwitchKey("?123", ""), LanguageKey(), SpaceKey(), ReturnKey()
            )
        )
    }

    private val space: TextKeyView by lazy { findViewById(R.id.button_space) }
    private val returnKey: ImageKeyView by lazy { findViewById(R.id.button_return) }

    override fun onReturnDrawableUpdate(returnDrawable: Int) {
        returnKey.img.imageResource = returnDrawable
    }

    override fun onInputMethodUpdate(ime: InputMethodEntry) {
        space.mainText.text = buildString {
            append(ime.displayName)
            ime.subMode.run { label.ifEmpty { name.ifEmpty { null } } }?.let { append(" ($it)") }
        }
    }
}
