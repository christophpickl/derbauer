package com.github.christophpickl.derbauer.view

import com.github.christophpickl.derbauer.logic.GameState
import javax.inject.Inject

class GameRenderer @Inject constructor(
    private val state: GameState
) {

    fun render(): String {
        return "You are in round ${state.round}"
    }

}
