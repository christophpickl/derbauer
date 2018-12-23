package com.github.christophpickl.derbauer.logic

class GameState {

    var round = 1
    val prompt = Prompt()
    val player = Player()

}

class Player {
    var gold = 500
    var land = 10
}

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
