package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.view.KeyboardEnterEvent
import com.github.christophpickl.derbauer.view.RenderEvent
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javax.inject.Inject

class GameEngine @Inject constructor(
    private val state: GameState,
    private val bus: EventBus
) {

    init {
        bus.register(this)
    }

    @Subscribe
    fun onKeyboardEnterEvent(event: KeyboardEnterEvent) {
        println(">> ${state.prompt.enteredText}")
        state.prompt.clear()
        bus.post(RenderEvent)
    }
}
