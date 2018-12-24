package com.github.christophpickl.derbauer.view

import com.github.christophpickl.derbauer.logic.ChooseScreen
import com.github.christophpickl.derbauer.model.State
import com.google.common.eventbus.EventBus
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javax.inject.Inject

class Keyboard @Inject constructor(
    private val bus: EventBus
) {

    fun onKeyEvent(event: KeyEvent) {
        if (!State.screen.promptEnabled || event.eventType != KeyEvent.KEY_PRESSED) {
            return
        }
        if (event.isPrintable && isValidInput(event)) {
            State.prompt.append(event.text!!.first())
            bus.post(RenderEvent)

        } else if (event.code == KeyCode.ENTER) {
            bus.post(KeyboardEnterEvent)

        } else if (event.code == KeyCode.BACK_SPACE) {
            if (State.prompt.maybeRemoveLast()) {
                bus.post(RenderEvent)
            }
        }
    }

    private fun isValidInput(event: KeyEvent) =
        State.screen !is ChooseScreen<*> || event.text.toIntOrNull() != null

    private val KeyEvent.isPrintable: Boolean
        get() =
            code.isLetterKey || code.isDigitKey || code == KeyCode.SPACE

}

object KeyboardEnterEvent
