package com.github.christophpickl.derbauer.logic.screens

class GameOverScreen : Screen {
    override val message = "Tadaaaaa! Game Over!"

    override val enableCancelOnEnter = false

    override fun onCallback(callback: ScreenCallback) {
        callback.onGameOver(this)
    }

}
