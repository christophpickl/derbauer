package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.service.AchievementScreen
import com.github.christophpickl.derbauer.logic.service.EndTurnScreen
import com.github.christophpickl.derbauer.logic.service.HappeningScreen
import com.github.christophpickl.derbauer.model.State


interface Screen {
    val message: String
    val enableCancelOnEnter: Boolean
    val promptEnabled: Boolean get() = true

    fun onCallback(callback: ScreenCallback)
}

interface Choice {
    val label: String
}

interface ScreenCallback {
    fun onHomeScreen(screen: HomeScreen)

    fun onTrade(screen: TradeScreen)
    fun onLandBuy(screen: LandBuyScreen)
    fun onLandSell(screen: LandSellScreen)
    fun onFoodBuy(screen: FoodBuyScreen)
    fun onFoodSell(screen: FoodSellScreen)

    fun onBuild(screen: BuildScreen)
    fun onUpgrade(screen: UpgradeScreen)
    fun onArmy(screen: ArmyScreen)

    fun onAchievement(screen: AchievementScreen)
    fun onEndTurn(screen: EndTurnScreen)
    fun onHappening(screen: HappeningScreen)
    fun onGameOver(screen: GameOverScreen)
}

interface ChooseScreen<C : Choice> : Screen {
    override val enableCancelOnEnter get() = true
    val choices: List<C>
}

interface NumberInputScreen : Screen {
    override val enableCancelOnEnter get() = true
}

interface ScreenController<S : Screen> {

}

interface ChooseScreenController<C : Choice, S : ChooseScreen<C>> : ScreenController<S> {
    fun select(choice: C)
}

interface EnummedChoice<E : Enum<E>> : Choice {
    val enum: Enum<E>
}

abstract class SimpleMessageScreen(
    private val state: State,
    override val message: String
) : Screen {

    override val enableCancelOnEnter = false

}
