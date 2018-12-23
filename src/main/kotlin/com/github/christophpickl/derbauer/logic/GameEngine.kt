package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.logic.screens.BuySellResourcesScreen
import com.github.christophpickl.derbauer.logic.screens.Choice
import com.github.christophpickl.derbauer.logic.screens.ChooseScreen
import com.github.christophpickl.derbauer.logic.screens.EndTurnScreen
import com.github.christophpickl.derbauer.logic.screens.FoodBuyScreen
import com.github.christophpickl.derbauer.logic.screens.FoodSellScreen
import com.github.christophpickl.derbauer.logic.screens.HomeScreen
import com.github.christophpickl.derbauer.logic.screens.LandBuyScreen
import com.github.christophpickl.derbauer.logic.screens.LandSellScreen
import com.github.christophpickl.derbauer.logic.screens.ScreenCallback
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

    override fun onHomeScreen(screen: HomeScreen) {
        val choice = maybeChoosenInput(screen) ?: return
        controllerRegistry.main.select(choice)
    }

    override fun onBuySellResources(screen: BuySellResourcesScreen) {
        val choice = maybeChoosenInput(screen) ?: return
        controllerRegistry.buySell.select(choice)
    }

    override fun onLandBuy(screen: LandBuyScreen) {
        val input = maybeNumberInput() ?: return
        controllerRegistry.buySell.buyLand(input)
    }

    override fun onLandSell(screen: LandSellScreen) {
        val input = maybeNumberInput() ?: return
        controllerRegistry.buySell.sellLand(input)
    }

    override fun onFoodBuy(screen: FoodBuyScreen) {
        val input = maybeNumberInput() ?: return
        controllerRegistry.buySell.buyFood(input)
    }

    override fun onFoodSell(screen: FoodSellScreen) {
        val input = maybeNumberInput() ?: return
        controllerRegistry.buySell.sellFood(input)
    }
    
    override fun onEndTurn(screen: EndTurnScreen) {
        state.day++
        state.screen = HomeScreen(state)
    }

    private fun maybeNumberInput(): Int? =
        state.prompt.enteredText.toIntOrNull() ?: beepReturn()

    private fun <C : Choice> maybeChoosenInput(screen: ChooseScreen<C>): C? {
        val index = state.prompt.enteredText.toIntOrNull() ?: return beepReturn()
        if (!(index >= 1 && index <= screen.choices.size)) return beepReturn()
        return screen.choices[index - 1]
    }

}
