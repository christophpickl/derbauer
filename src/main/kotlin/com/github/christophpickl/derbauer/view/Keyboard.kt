package com.github.christophpickl.derbauer.view

import com.github.christophpickl.derbauer.logic.GameState
import com.github.christophpickl.derbauer.logic.screens.ChooseScreen
import com.google.common.eventbus.EventBus
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javax.inject.Inject

class Keyboard @Inject constructor(
    private val state: GameState,
    private val bus: EventBus
) {

    fun onKeyEvent(event: KeyEvent) {
        if (event.eventType != KeyEvent.KEY_PRESSED) {
            return
        }

        if (event.isPrintable) {
            if (!isValid(event)) return
            state.prompt.append(event.text!!.first())
            bus.post(RenderEvent)

        } else if (event.code == KeyCode.ENTER) {
            bus.post(KeyboardEnterEvent)

        } else if (event.code == KeyCode.BACK_SPACE) {
            if (state.prompt.maybeRemoveLast()) {
                bus.post(RenderEvent)
            }
        }
    }

    private fun isValid(event: KeyEvent): Boolean {
        val screen = state.screen
        if (screen is ChooseScreen<*> && !event.code.isDigitKey) {
            return false
        }
        return true
    }

    private val KeyEvent.isPrintable: Boolean get() = code.isLetterKey || code.isDigitKey || code == KeyCode.SPACE

}

object KeyboardEnterEvent
