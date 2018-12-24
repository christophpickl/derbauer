package com.github.christophpickl.derbauer.misc

import com.github.christophpickl.derbauer.logic.Screen
import com.github.christophpickl.derbauer.logic.ScreenCallback

class GameOver : Screen {
    override val message = "Tadaaaaa! Game Over!"

    override val enableCancelOnEnter = false

    override fun onCallback(callback: ScreenCallback) {
        callback.onGameOver(this)
    }

}
