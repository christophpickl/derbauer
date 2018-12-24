package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.achievement.AchievementScreen
import com.github.christophpickl.derbauer.army.ArmyScreen
import com.github.christophpickl.derbauer.army.HireSoldiersScreen
import com.github.christophpickl.derbauer.build.BuildScreen
import com.github.christophpickl.derbauer.happening.Happener
import com.github.christophpickl.derbauer.happening.HappeningScreen
import com.github.christophpickl.derbauer.misc.EndTurn
import com.github.christophpickl.derbauer.misc.EndTurnScreen
import com.github.christophpickl.derbauer.misc.GameOver
import com.github.christophpickl.derbauer.misc.HomeScreen
import com.github.christophpickl.derbauer.model.State
import com.github.christophpickl.derbauer.trade.FoodBuyScreen
import com.github.christophpickl.derbauer.trade.FoodSellScreen
import com.github.christophpickl.derbauer.trade.LandBuyScreen
import com.github.christophpickl.derbauer.trade.LandSellScreen
import com.github.christophpickl.derbauer.trade.TradeScreen
import com.github.christophpickl.derbauer.upgrade.UpgradeScreen
import com.github.christophpickl.derbauer.view.KeyboardEnterEvent
import com.github.christophpickl.derbauer.view.RenderEvent
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import mu.KotlinLogging.logger
import javax.inject.Inject

@Deprecated(message = "v2")
class Router @Inject constructor(
    private val bus: EventBus,
    private val controllerRegistry: ScreenControllerRegistry,
    private val endTurn: EndTurn,
    private val happener: Happener

) : ScreenCallback {

    private val log = logger {}

    init {
        bus.register(this)
    }

    @Subscribe
    fun onKeyboardEnterEvent(@Suppress("UNUSED_PARAMETER") event: KeyboardEnterEvent) {
        log.info("Entered: '${State.prompt.enteredText}'")

        if (State.screen.enableCancelOnEnter && State.prompt.enteredText.isEmpty()) {
            log.debug { "Empty entered, going to cancel back to home screen." }
            State.screen = HomeScreen()
        } else {
            State.screen.onCallback(this)
        }

        State.prompt.clear()
        bus.post(RenderEvent)
    }

    override fun onHomeScreen(screen: HomeScreen) {
        val choice = maybeChoosenInput(screen) ?: return
        controllerRegistry.main.select(choice)
    }

    override fun onUpgrade(screen: UpgradeScreen) {
        val choice = maybeChoosenInput(screen) ?: return
        controllerRegistry.upgrade.select(choice)
    }

    override fun onBuild(screen: BuildScreen) {
        val choice = maybeChoosenInput(screen) ?: return
        controllerRegistry.build.select(choice)
    }

    // TRADE ===========================================================================================================

    override fun onTrade(screen: TradeScreen) {
        val choice = maybeChoosenInput(screen) ?: return
        controllerRegistry.trade.select(choice)
    }

    override fun onLandBuy(screen: LandBuyScreen) {
        val input = maybeNumberInput() ?: return
        controllerRegistry.trade.buyLand(input)
    }

    override fun onLandSell(screen: LandSellScreen) {
        val input = maybeNumberInput() ?: return
        controllerRegistry.trade.sellLand(input)
    }

    override fun onFoodBuy(screen: FoodBuyScreen) {
        val input = maybeNumberInput() ?: return
        controllerRegistry.trade.buyFood(input)
    }

    override fun onFoodSell(screen: FoodSellScreen) {
        val input = maybeNumberInput() ?: return
        controllerRegistry.trade.sellFood(input)
    }

    // ARMY ============================================================================================================

    override fun onArmy(screen: ArmyScreen) {
        val choice = maybeChoosenInput(screen) ?: return
        controllerRegistry.army.select(choice)
    }

    override fun onHireSoldiers(screen: HireSoldiersScreen) {
        val input = maybeNumberInput() ?: return
        controllerRegistry.army.hireSoldier(input)
    }

    // END =============================================================================================================

    override fun onEndTurn(screen: EndTurnScreen) {
        log.trace { "onEndTurn()" }
        State.day++
        State.screen = happener.letItHappen() ?: HomeScreen()
    }

    override fun onAchievement(screen: AchievementScreen) {
        State.screen = endTurn.calculateEndTurn()
    }

    override fun onHappening(screen: HappeningScreen) {
        State.screen = HomeScreen()
    }

    override fun onGameOver(screen: GameOver) {
        State.reset()
        State.screen = HomeScreen()
    }


    // =================================================================================================================

    private fun maybeNumberInput(): Int? =
        State.prompt.enteredText.toIntOrNull() ?: beepReturn()

    private fun <C : Choice> maybeChoosenInput(screen: ChooseScreen<C>): C? {
        val index = State.prompt.enteredText.toIntOrNull() ?: return beepReturn()
        if (!(index >= 1 && index <= screen.choices.size)) return beepReturn()
        return screen.choices[index - 1]
    }

}
