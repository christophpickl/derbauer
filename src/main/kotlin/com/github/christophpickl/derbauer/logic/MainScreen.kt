package com.github.christophpickl.derbauer.logic

import javax.inject.Inject
import kotlin.reflect.KMutableProperty1

class MainScreen(
    private val state: GameState
) : ChooseScreen<MainScreenChoice> {

    override val message = "What do you wanna do now?"

    override val choices
        get() = listOf(
            MainScreenChoice(MainScreenChoiceEnum.BuyLand, "Buy land (${state.prizes.landBuy}$)"),
            MainScreenChoice(MainScreenChoiceEnum.SellLand, "Sell land (${state.prizes.landSell}$)"),
            MainScreenChoice(MainScreenChoiceEnum.BuyFood, "Buy food (${state.prizes.foodBuy}$)"),
            MainScreenChoice(MainScreenChoiceEnum.SellFood, "Sell food (${state.prizes.foodSell}$)"),
            MainScreenChoice(MainScreenChoiceEnum.EndTurn, "End Turn")
        )

    override fun onCallback(callback: ScreenCallback) {
        callback.onMainScreen(this)
    }

    enum class MainScreenChoiceEnum {
        BuyLand,
        SellLand,
        BuyFood,
        SellFood,
        EndTurn
    }
}

class MainScreenChoice(
    override val enum: Enum<MainScreen.MainScreenChoiceEnum>,
    override val label: String
) : EnummedChoice<MainScreen.MainScreenChoiceEnum>

class MainScreenController @Inject constructor(
    private val state: GameState,
    private val happener: EndTurnHappener
) : ChooseScreenController<MainScreenChoice, MainScreen> {

    private val numberWidth = 6
    private val numberWidthGrow = 5 // plus sign

    override fun select(choice: MainScreenChoice) {
        val nextScreen = when (choice.enum) {
            MainScreen.MainScreenChoiceEnum.BuyLand -> LandBuyScreen(state)
            MainScreen.MainScreenChoiceEnum.SellLand -> LandSellScreen(state)
            MainScreen.MainScreenChoiceEnum.BuyFood -> FoodBuyScreen(state)
            MainScreen.MainScreenChoiceEnum.SellFood -> FoodSellScreen(state)
            MainScreen.MainScreenChoiceEnum.EndTurn -> calculateEndTurn()
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


    fun buyLand(amount: Int) {
        buySellOperation(
            isBuying = true,
            amount = amount,
            costsPerItem = state.prizes.landBuy,
            targetProperty = Player::land
        )
    }

    fun sellLand(amount: Int) {
        buySellOperation(
            isBuying = false,
            amount = amount,
            costsPerItem = state.prizes.landSell,
            targetProperty = Player::land
        )
    }

    fun buyFood(amount: Int) {
        buySellOperation(
            isBuying = true,
            amount = amount,
            costsPerItem = state.prizes.foodBuy,
            targetProperty = Player::food
        )
    }

    fun sellFood(amount: Int) {
        buySellOperation(
            isBuying = false,
            amount = amount,
            costsPerItem = state.prizes.foodSell,
            targetProperty = Player::food
        )
    }

    private fun buySellOperation(isBuying: Boolean, amount: Int, targetProperty: KMutableProperty1<Player, Int>, costsPerItem: Int) {
        val goldChanging = if (isBuying) {
            val costs = canAffordCalcCosts(costsPerItem, amount) ?: return
            targetProperty.increment(state.player, amount)
            costs
        } else {
            val income = hasEnoughCalcIncome(costsPerItem, amount, targetProperty.get(state.player)) ?: return
            targetProperty.decrement(state.player, amount)
            income
        }
        state.player.gold = state.player.gold + if (isBuying) (-1 * goldChanging) else goldChanging
        state.screen = MainScreen(state)
    }

    private fun canAffordCalcCosts(costsPerItem: Int, amount: Int): Int? {
        val costs = costsPerItem * amount
        if (costs > state.player.gold) {
            return beepReturn()
        }
        return costs
    }

    private fun hasEnoughCalcIncome(costsPerItem: Int, amount: Int, has: Int): Int? {
        if (has < amount) {
            return beepReturn()
        }
        return costsPerItem * amount
    }


}

class EndTurnScreen(
    override val message: String
) : Screen {

    override fun onCallback(callback: ScreenCallback) {
        callback.onEndTurn(this)
    }
}
