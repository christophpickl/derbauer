package com.github.christophpickl.derbauer2.ui

import com.github.christophpickl.derbauer2.misc.Listener
import com.github.christophpickl.derbauer2.misc.Subscription
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.model.Model
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

interface PromptListener : Listener {
    fun onTextChange(text: String)
    fun onEnter(input: PromptInput)
}

sealed class PromptInput {
    companion object {
        fun by(text: String) = if (text.isEmpty()) Empty else Number(text.toInt())
    }

    object Empty : PromptInput() {
        override fun toString() = "Empty"
    }

    class Number(val number: Int) : PromptInput() {
        override fun toString() = "Number{$number}"
    }
}

enum class PromptMode {
    Off,
    Enter,
    Input
}

class Prompt : KeyboardListener {

    val subscription = Subscription<PromptListener>()
    var enteredText = ""

    override fun onKeyboard(event: KeyboardEvent) {
        when (Model.currentView.promptMode) {
            PromptMode.Off -> {
                // do nothing
            }
            PromptMode.Enter -> {
                if (event == KeyboardEvent.Enter) {
                    subscription.broadcast { onEnter(PromptInput.Empty) }
                } else {
                }
            }
            PromptMode.Input -> {
                when (event) {
                    KeyboardEvent.Enter -> {
                        val text = enteredText
                        enteredText = ""
                        subscription.broadcast { onEnter(PromptInput.by(text)) }
                    }
                    KeyboardEvent.Backspace -> {
                        if (enteredText.isEmpty()) {
                            // do nothing
                        } else {
                            enteredText = enteredText.substring(0, enteredText.length - 1)
                            subscription.broadcast { onTextChange(enteredText) }
                        }
                    }
                    is KeyboardEvent.Input -> {
                        enteredText = "$enteredText${event.digit}"
                        subscription.broadcast { onTextChange(enteredText) }
                    }
                }.enforceWhenBranches()
            }
        }.enforceWhenBranches()
    }
}

interface KeyboardListener : Listener {
    fun onKeyboard(event: KeyboardEvent)
}

sealed class KeyboardEvent {
    object Enter : KeyboardEvent()
    object Backspace : KeyboardEvent()
    class Input(val digit: Int) : KeyboardEvent()
}

class Keyboard : KeyAdapter() {

    val subscription = Subscription<KeyboardListener>()

    override fun keyPressed(e: KeyEvent) {
        if (e.keyCode == KeyEvent.VK_ENTER) {
            subscription.broadcast { onKeyboard(KeyboardEvent.Enter) }
        } else if (e.keyCode == KeyEvent.VK_BACK_SPACE) {
            subscription.broadcast { onKeyboard(KeyboardEvent.Backspace) }
        }
    }

    override fun keyTyped(e: KeyEvent) {
        if (e.isDigit) {
            subscription.broadcast { onKeyboard(KeyboardEvent.Input(e.keyChar.toString().toInt())) }
        }
    }
}

private val KeyEvent.isDigit get() = keyChar.category == CharCategory.DECIMAL_DIGIT_NUMBER
