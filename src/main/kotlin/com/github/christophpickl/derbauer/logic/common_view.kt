package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.achievement.AchievementScreen
import com.github.christophpickl.derbauer.army.ArmyScreen
import com.github.christophpickl.derbauer.army.HireSoldiersScreen
import com.github.christophpickl.derbauer.build.BuildScreen
import com.github.christophpickl.derbauer.happening.HappeningScreen
import com.github.christophpickl.derbauer.misc.EndTurnScreen
import com.github.christophpickl.derbauer.misc.GameOver
import com.github.christophpickl.derbauer.misc.HomeScreen
import com.github.christophpickl.derbauer.trade.FoodBuyScreen
import com.github.christophpickl.derbauer.trade.FoodSellScreen
import com.github.christophpickl.derbauer.trade.LandBuyScreen
import com.github.christophpickl.derbauer.trade.LandSellScreen
import com.github.christophpickl.derbauer.trade.TradeScreen
import com.github.christophpickl.derbauer.upgrade.UpgradeScreen

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
    fun onHireSoldiers(screen: HireSoldiersScreen)

    fun onAchievement(screen: AchievementScreen)
    fun onEndTurn(screen: EndTurnScreen)
    fun onHappening(screen: HappeningScreen)
    fun onGameOver(screen: GameOver)
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
    override val message: String
) : Screen {

    override val enableCancelOnEnter = false

}
