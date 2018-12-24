package com.github.christophpickl.derbauer.logic

@Deprecated(message = "v2")
class Prompt {

    private var _enteredText = ""
    val enteredText get() = _enteredText

    fun append(text: Char) {
        _enteredText = "$_enteredText$text"
    }

    fun clear() {
        _enteredText = ""
    }

    fun maybeRemoveLast(): Boolean {
        if (_enteredText.isEmpty()) {
            return false
        }
        _enteredText = _enteredText.substring(0, _enteredText.length - 1)
        return true
    }

}
