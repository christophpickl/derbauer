package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.State

class GameOverScreen(
    private val state: State
) : Screen {
    override val message = "Tadaaaaa! Game Over!"

    override val enableCancelOnEnter = false

    override fun onCallback(callback: ScreenCallback) {
        callback.onGameOver(this)
    }

}
