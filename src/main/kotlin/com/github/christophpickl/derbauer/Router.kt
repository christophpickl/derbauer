package com.github.christophpickl.derbauer

import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.PromptListener
import com.github.christophpickl.derbauer.ui.Renderer
import mu.KotlinLogging.logger

class Router(
    private val renderer: Renderer,
    private val controllerRegistry: Registry = Registry(renderer)
) : PromptListener, ViewCallback by controllerRegistry {

    private val log = logger {}

    override fun onTextChange(text: String) {
        renderer.render()
    }

    override fun onEnter(input: PromptInput) {
        log.debug { "onEnter($input) => ${Model.currentView::class.simpleName}" }
        Model.currentView.onCallback(this, input)
        renderer.render()
    }

}
