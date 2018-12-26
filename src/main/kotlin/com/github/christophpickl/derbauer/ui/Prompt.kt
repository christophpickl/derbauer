package com.github.christophpickl.derbauer.ui

import com.github.christophpickl.derbauer.misc.enforceWhenBranches
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.misc.Dispatcher
import com.github.christophpickl.kpotpourri.common.misc.DispatcherListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

interface PromptListener : DispatcherListener {
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

    val dispatcher = Dispatcher<PromptListener>()
    var enteredText = ""

    override fun onKeyboard(event: KeyboardEvent) {
        when (Model.currentView.promptMode) {
            PromptMode.Off -> {
                // ignore
            }
            PromptMode.Enter -> {
                if (event == KeyboardEvent.Enter) {
                    dispatcher.dispatch { onEnter(PromptInput.Empty) }
                } else {
                    // ignore
                }
            }
            PromptMode.Input -> {
                handleInput(event)
            }
        }.enforceWhenBranches()
    }

    private fun handleInput(event: KeyboardEvent) {
        when (event) {
            KeyboardEvent.Enter -> {
                val text = enteredText
                enteredText = ""
                dispatcher.dispatch { onEnter(PromptInput.by(text)) }
            }
            KeyboardEvent.Backspace -> {
                if (enteredText.isEmpty()) {
                    // ignore
                } else {
                    enteredText = enteredText.dropLast(1)
                    dispatcher.dispatch { onTextChange(enteredText) }
                }
            }
            is KeyboardEvent.Input -> {
                enteredText = "$enteredText${event.digit}"
                dispatcher.dispatch { onTextChange(enteredText) }
            }
        }.enforceWhenBranches()
    }
}

interface KeyboardListener : DispatcherListener {
    fun onKeyboard(event: KeyboardEvent)
}

sealed class KeyboardEvent {
    object Enter : KeyboardEvent()
    object Backspace : KeyboardEvent()
    class Input(val digit: Int) : KeyboardEvent()
}

class Keyboard : KeyAdapter() {

    val dispatcher = Dispatcher<KeyboardListener>()

    override fun keyPressed(e: KeyEvent) {
        if (e.keyCode == KeyEvent.VK_ENTER) {
            dispatcher.dispatch { onKeyboard(KeyboardEvent.Enter) }
        } else if (e.keyCode == KeyEvent.VK_BACK_SPACE) {
            dispatcher.dispatch { onKeyboard(KeyboardEvent.Backspace) }
        }
    }

    override fun keyTyped(e: KeyEvent) {
        if (e.isDigit) {
            dispatcher.dispatch { onKeyboard(KeyboardEvent.Input(e.keyChar.toString().toInt())) }
        }
    }
}

private val KeyEvent.isDigit get() = keyChar.category == CharCategory.DECIMAL_DIGIT_NUMBER
