package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.view.KeyboardEnterEvent
import com.github.christophpickl.derbauer.view.RenderEvent
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import mu.KotlinLogging.logger
import javax.inject.Inject

class GameEngine @Inject constructor(
    private val state: GameState,
    private val bus: EventBus,
    private val controllerRegistry: ScreenControllerRegistry
) : ScreenCallback {

    private val log = logger {}
    
    init {
        bus.register(this)
    }

    @Subscribe
    fun onKeyboardEnterEvent(@Suppress("UNUSED_PARAMETER") event: KeyboardEnterEvent) {
        log.info("Entered: '${state.prompt.enteredText}'")
        state.screen.onCallback(this)
        state.prompt.clear()
        bus.post(RenderEvent)
    }

    override fun onMainScreen(screen: MainScreen) {
        val choice = maybeChoosenInput(screen) ?: return
        controllerRegistry.mainScreen.select(choice)
    }

    override fun onLandBuy(screen: LandBuyScreen) {
        val input = maybeNumberInput() ?: return
        controllerRegistry.mainScreen.buyLand(input)
    }

    override fun onLandSell(screen: LandSellScreen) {
        val input = maybeNumberInput() ?: return
        controllerRegistry.mainScreen.sellLand(input)
    }

    override fun onEndTurn(screen: EndTurnScreen) {
        state.day++
        state.screen = MainScreen(state)
    }

    private fun maybeNumberInput(): Int? =
        state.prompt.enteredText.toIntOrNull() ?: beepReturn()

    private fun <C : Choice> maybeChoosenInput(screen: ChooseScreen<C>): C? {
        val index = state.prompt.enteredText.toIntOrNull() ?: return beepReturn()
        if (!(index >= 1 && index <= screen.choices.size)) return beepReturn()
        return screen.choices[index - 1]
    }

}
