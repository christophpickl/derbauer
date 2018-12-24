package com.github.christophpickl.derbauer.misc

import com.github.christophpickl.derbauer.achievement.AchievementHappener
import com.github.christophpickl.derbauer.army.ArmyScreen
import com.github.christophpickl.derbauer.build.BuildScreen
import com.github.christophpickl.derbauer.logic.ChooseScreen
import com.github.christophpickl.derbauer.logic.ChooseScreenController
import com.github.christophpickl.derbauer.logic.EnummedChoice
import com.github.christophpickl.derbauer.logic.ScreenCallback
import com.github.christophpickl.derbauer.model.State
import com.github.christophpickl.derbauer.trade.TradeScreen
import com.github.christophpickl.derbauer.upgrade.UpgradeScreen
import javax.inject.Inject

class HomeScreen : ChooseScreen<HomeChoice> {

    private val messages = listOf(
        "What are we up to today?",
        "What can I do for you, master?",
        "Your wish is my command."
    )
    override val message = messages.random()

    override val choices
        get() = listOf(
            HomeChoice(HomeEnum.Trade, "Trade"),
            HomeChoice(HomeEnum.Build, "Build${if (State.player.landAvailable == 0) " (no land available)" else ""}"),
            HomeChoice(HomeEnum.Upgrade, "Upgrade"),
            HomeChoice(HomeEnum.Army, "Military"),
            HomeChoice(HomeEnum.EndTurn, "End Turn")
        )

    override fun onCallback(callback: ScreenCallback) {
        callback.onHomeScreen(this)
    }

}

enum class HomeEnum {
    Trade,
    Build,
    Upgrade,
    Army,
    EndTurn
}

class HomeChoice(
    override val enum: Enum<HomeEnum>,
    override val label: String
) : EnummedChoice<HomeEnum>

class MainController @Inject constructor(
    private val endTurn: EndTurn,
    private val happening: AchievementHappener
) : ChooseScreenController<HomeChoice, HomeScreen> {

    override fun select(choice: HomeChoice) {
        val nextScreen = when (choice.enum) {
            HomeEnum.Trade -> TradeScreen()
            HomeEnum.Build -> BuildScreen()
            HomeEnum.Upgrade -> UpgradeScreen()
            HomeEnum.Army -> ArmyScreen()
            // FIXME also react on ENTER! directly
            HomeEnum.EndTurn -> happening.anyHappened()?.let { it } ?: endTurn.calculateEndTurn()
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        State.screen = nextScreen
    }

}
