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

class GameOverScreen : InfoScreen(buildMessage()) {
    companion object {
        fun buildMessage(): String {
            val suffix = when (Model.global.day) {
                in 0..5 -> "you looser!"
                in 6..10 -> "you beginner."
                in 11..20 -> "you seem to get the grasp of it."
                in 21..40 -> "of great leadership!"
                else -> "of bad ass ruler skills!!!!!!!!11111elf"
            }
            return "Game over!\n\n" +
                "${AsciiArt.gameOver}\n\n" +
                "R.I.P. after ${Model.global.day} days $suffix"
        }
    }

    override val promptMode = PromptMode.Off
    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        // no-op
    }
}
