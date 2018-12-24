package com.github.christophpickl.derbauer2.endturn

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.AsciiArt
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.PromptMode
import com.github.christophpickl.derbauer2.ui.screen.InfoScreen

class EndTurnScreen(message: String) : InfoScreen(message) {
    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        callback.onEndTurn()
    }
}

class GameOverScreen : InfoScreen(
    "Game over!\n\n" +
        "${AsciiArt.gameOver}\n\n" +
        "R.I.P. after ${Model.global.day} days of great leadership."
) {
    override val promptMode = PromptMode.Off
    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
    }
}
