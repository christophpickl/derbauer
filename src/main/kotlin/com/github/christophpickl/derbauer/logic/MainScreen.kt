package com.github.christophpickl.derbauer.logic

import javax.inject.Inject

class MainScreen(
    private val state: GameState
) : ChooseScreen<MainScreenChoice> {

    override val message = "What do you wanna do now?"

    override val choices
        get() = listOf(
            MainScreenChoice(MainScreenChoiceEnum.BuyLand, "Buy land (${state.prizes.landBuy}$)"),
            MainScreenChoice(MainScreenChoiceEnum.SellLand, "Sell land (${state.prizes.landSell}$)"),
            MainScreenChoice(MainScreenChoiceEnum.EndTurn, "End Turn")
        )

    override fun onCallback(callback: ScreenCallback) {
        callback.onMainScreen(this)
    }

    enum class MainScreenChoiceEnum {
        BuyLand,
        SellLand,
        EndTurn
    }
}

class MainScreenChoice(
    override val enum: Enum<MainScreen.MainScreenChoiceEnum>,
    override val label: String
) : EnummedChoice<MainScreen.MainScreenChoiceEnum>

class MainScreenController @Inject constructor(
    private val state: GameState
) : ChooseScreenController<MainScreenChoice, MainScreen> {

    private val numberWidth = 6
    private val numberWidthGrow = 5 // plus sign

    override fun select(choice: MainScreenChoice) {
        val nextScreen = when (choice.enum) {
            MainScreen.MainScreenChoiceEnum.BuyLand -> LandBuyScreen(state)
            MainScreen.MainScreenChoiceEnum.SellLand -> LandSellScreen(state)
            MainScreen.MainScreenChoiceEnum.EndTurn -> calculateEndTurn()
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        state.screen = nextScreen
    }

    private fun calculateEndTurn(): EndTurnScreen {
        val foodIncome = state.player.people / 3
        val peopleIncome = state.player.land / 10
        val goldIncome = 2
        val message = """Your daily end turn report:
            |
            |${formatGrowth("Food production", state.player.food, foodIncome)}
            |${formatGrowth("People growth  ", state.player.people, peopleIncome)}
            |${formatGrowth("Gold income    ", state.player.gold, goldIncome)}
            | 
            |It was quiet and calm.
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
    
    
    fun buyLand(amount: Int) {
        val costs = state.prizes.landBuy * amount
        if (costs > state.player.gold) {
            return beep()
        }
        state.player.land += amount
        state.player.gold -= costs
        state.screen = MainScreen(state)
    }

    fun sellLand(amount: Int) {
        if (state.player.land < amount) {
            return beep()
        }
        state.player.land -= amount
        state.player.gold += state.prizes.landSell * amount
        state.screen = MainScreen(state)
    }

}

class LandBuyScreen(
    state: GameState
) : NumberInputScreen {
    override val message = "How much land do you wanna buy?\n" +
        "1 costs ${state.prizes.landBuy} gold, you can afford ${state.affordableLand} land."
    override fun onCallback(callback: ScreenCallback) {
        callback.onLandBuy(this)
    }
}

class LandSellScreen(
    state: GameState
) : NumberInputScreen {
    override val message = "How much land do you wanna sell?\n1 for ${state.prizes.landSell} gold, you've got ${state.player.land} land."
    override fun onCallback(callback: ScreenCallback) {
        callback.onLandSell(this)
    }
}

class EndTurnScreen(
    override val message: String
) : Screen {

    override fun onCallback(callback: ScreenCallback) {
        callback.onEndTurn(this)
    }
}
