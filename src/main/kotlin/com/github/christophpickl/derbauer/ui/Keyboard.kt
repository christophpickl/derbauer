package com.github.christophpickl.derbauer.ui

import com.github.christophpickl.kpotpourri.common.misc.Dispatcher
import com.github.christophpickl.kpotpourri.common.misc.DispatcherListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class Keyboard(
    private val allowedToType: (KeyEvent) -> Boolean
) : KeyAdapter() {

    val dispatcher = Dispatcher<KeyboardListener>()

    override fun keyPressed(e: KeyEvent) {
        if (e.keyCode == KeyEvent.VK_ENTER) {
            dispatcher.dispatch { onKeyboard(KeyboardEvent.Enter) }
        } else if (e.keyCode == KeyEvent.VK_BACK_SPACE) {
            dispatcher.dispatch { onKeyboard(KeyboardEvent.Backspace) }
        }
    }

    override fun keyTyped(e: KeyEvent) {
        if (allowedToType(e)) {
            dispatcher.dispatch { onKeyboard(KeyboardEvent.Input(e.keyChar)) }
        }
    }
}

interface KeyboardListener : DispatcherListener {
    fun onKeyboard(event: KeyboardEvent)
}

sealed class KeyboardEvent {
    object Enter : KeyboardEvent()
    object Backspace : KeyboardEvent()
    class Input(val digit: Char) : KeyboardEvent()
}

val KeyEvent.isDigit get() = keyChar.category == CharCategory.DECIMAL_DIGIT_NUMBER
val KeyEvent.isLowercaseLetter get() = keyChar.category == CharCategory.LOWERCASE_LETTER
