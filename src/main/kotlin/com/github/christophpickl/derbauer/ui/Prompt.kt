package com.github.christophpickl.derbauer.ui

import com.github.christophpickl.derbauer.misc.enforceWhenBranches
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.AmountType
import com.github.christophpickl.kpotpourri.common.misc.Dispatcher
import com.github.christophpickl.kpotpourri.common.misc.DispatcherListener

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
                    dispatcher.dispatch { onEnter(RawPromptInput.Empty) }
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
                dispatcher.dispatch {
                    onEnter(RawPromptInput.by(text))
                }
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

interface PromptListener : DispatcherListener {
    fun onTextChange(text: String)
    fun onEnter(input: RawPromptInput)
}

sealed class PromptInput {

    object Empty : PromptInput() {
        override fun toString() = "Empty"
    }

    data class Number(val number: Long) : PromptInput() {
        override fun toString() = "Number{$number}"
    }

}

sealed class RawPromptInput {

    companion object {
        fun by(text: String): RawPromptInput = if (text.isEmpty()) {
            Empty
        } else {
            parse(text)?.let {
                Number(it)
            } ?: Invalid(text)
        }

        private fun parse(text: String): Long? {
            text.toLongOrNull()?.let {
                return it
            }
            return AmountType.valuesButSingle.mapNotNull { type ->
                type.regexp.matchEntire(text)?.let {
                    type to it
                }
            }.firstOrNull()?.let { (type, match) ->
                match.groupValues[1].toLong() * type.thousands
            }
        }

    }

    object Empty : RawPromptInput() {
        override fun toString() = "Empty"
    }

    data class Number(val number: Long) : RawPromptInput() {
        override fun toString() = "Number{$number}"
    }

    data class Invalid(val entered: String) : RawPromptInput() {
        override fun toString() = "Invalid{$entered}"
    }
}

enum class PromptMode {
    Off,
    Enter,
    Input
}
