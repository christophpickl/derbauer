package com.github.christophpickl.derbauer2

import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.PromptListener
import com.github.christophpickl.derbauer2.ui.Renderer
import mu.KotlinLogging.logger

class Router(
    private val renderer: Renderer,
    private val controllerRegistry: Registry = Registry(renderer)
) : PromptListener, ScreenCallback by controllerRegistry {

    private val log = logger {}

    override fun onTextChange(text: String) {
        renderer.render()
    }

    override fun onEnter(input: PromptInput) {
        log.debug { "onEnter($input) => ${Model.screen::class.simpleName}" }
        Model.screen.onCallback(this, input)
        renderer.render()
    }

}
