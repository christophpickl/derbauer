package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.EndTurnHappener
import com.github.christophpickl.derbauer.logic.GameState
import com.github.christophpickl.derbauer.logic.formatNumber
import javax.inject.Inject

class HomeScreen(
    private val state: GameState
) : ChooseScreen<HomeChoice> {

    override val message = "What do you wanna do now?"

    override val choices
        get() = listOf(
            HomeChoice(HomeEnum.BuySell, "Buy/sell stuff"),
            HomeChoice(HomeEnum.EndTurn, "End Turn")
        )

    override fun onCallback(callback: ScreenCallback) {
        callback.onHomeScreen(this)
    }

}

enum class HomeEnum {
    BuySell,
    EndTurn
}

class HomeChoice(
    override val enum: Enum<HomeEnum>,
    override val label: String
) : EnummedChoice<HomeEnum>

class MainController @Inject constructor(
    private val state: GameState,
    private val happener: EndTurnHappener
) : ChooseScreenController<HomeChoice, HomeScreen> {

    private val numberWidth = 6
    private val numberWidthGrow = 5 // plus sign

    override fun select(choice: HomeChoice) {
        val nextScreen = when (choice.enum) {
            HomeEnum.BuySell -> BuySellResourcesScreen(state)
            HomeEnum.EndTurn -> calculateEndTurn()
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        state.screen = nextScreen
    }

    private fun calculateEndTurn(): EndTurnScreen {
        val maybeHappenedMessage = happener.letItHappen()
        val foodIncome = state.player.people / 3
        val peopleIncome = state.player.land / 10
        val goldIncome = 2
        val message = """Your daily end turn report:
            |
            |${formatGrowth("Food production", state.player.food, foodIncome)}
            |${formatGrowth("People growth  ", state.player.people, peopleIncome)}
            |${formatGrowth("Gold income    ", state.player.gold, goldIncome)}
            | 
            |${maybeHappenedMessage ?: "It was quiet and calm."}
            |
            |Hit ENTER to continue.
        """.trimMargin()
        state.player.food += foodIncome
        state.player.people += peopleIncome
        state.player.gold += goldIncome
        return EndTurnScreen(message)
    }

    private fun formatGrowth(label: String, current: Int, growth: Int) =
        "$label: ${formatNumber(current, numberWidth)} => " +
            "${formatNumber(growth, numberWidthGrow, addPlusSign = true)} => " +
            formatNumber(current + growth, numberWidth)

}

class EndTurnScreen(
    override val message: String
) : Screen {

    override fun onCallback(callback: ScreenCallback) {
        callback.onEndTurn(this)
    }
}
