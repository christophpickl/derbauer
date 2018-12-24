package com.github.christophpickl.derbauer2.misc

import com.github.christophpickl.derbauer2.State
import mu.KotlinLogging.logger

class Router(
    private val renderer: Renderer,
    private val controllerRegistry: Registry = Registry()
) : PromptListener, ScreenCallback by controllerRegistry {

    private val log = logger {}

    override fun onTextChange(text: String) {
        renderer.render()
    }

    override fun onEnter(input: PromptInput) {
        log.debug { "onEnter($input)" }
        State.screen.onCallback(this, input)
        renderer.render()
    }

}
