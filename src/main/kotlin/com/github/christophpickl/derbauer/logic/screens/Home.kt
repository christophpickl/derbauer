package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.TurnFinisher
import com.github.christophpickl.derbauer.model.State
import javax.inject.Inject

class HomeScreen(
    private val state: State
) : ChooseScreen<HomeChoice> {

    override val message = "What do you wanna do now?"

    override val choices
        get() = listOf(
            HomeChoice(HomeEnum.Trade, "Trade resources"),
            HomeChoice(HomeEnum.Build, "Build buildings"),
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
    private val state: State,
    private val turnFinisher: TurnFinisher
) : ChooseScreenController<HomeChoice, HomeScreen> {

    override fun select(choice: HomeChoice) {
        val nextScreen = when (choice.enum) {
            HomeEnum.Trade -> TradeScreen(state)
            HomeEnum.Build -> BuildScreen(state)
            HomeEnum.Upgrade -> UpgradeScreen(state)
            HomeEnum.Army -> ArmyScreen(state)
            HomeEnum.EndTurn -> turnFinisher.calculateEndTurn()
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        state.screen = nextScreen
    }

}

class EndTurnScreen(
    override val message: String
) : Screen {
    override val enableCancelOnEnter = true

    override fun onCallback(callback: ScreenCallback) {
        callback.onEndTurn(this)
    }
}
