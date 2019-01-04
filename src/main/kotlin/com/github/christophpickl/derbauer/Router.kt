package com.github.christophpickl.derbauer

import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.Beeper
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.PromptListener
import com.github.christophpickl.derbauer.ui.RawPromptInput
import com.github.christophpickl.derbauer.ui.RealBeeper
import com.github.christophpickl.derbauer.ui.Renderer
import mu.KotlinLogging.logger

class Router(
    private val renderer: Renderer,
    private val controllerRegistry: Registry = Registry(renderer),
    private val beeper: Beeper = RealBeeper
) : PromptListener, ViewCallback by controllerRegistry {

    private val log = logger {}

    override fun onTextChange(text: String) {
        renderer.render()
    }

    override fun onEnter(input: RawPromptInput) {
        log.debug { "onEnter($input) => Current view: ${Model.currentView::class.simpleName}" }
        val validInput = when (input) {
            is RawPromptInput.Empty -> PromptInput.Empty
            is RawPromptInput.Number -> PromptInput.Number(input.number)
            is RawPromptInput.Invalid -> {
                beeper.beep("Invalid input: [${input.entered}]")
                renderer.render()
                return
            }
        }
        Model.currentView.onCallback(this, validInput)
        renderer.render() // must be after callback!
    }

}
