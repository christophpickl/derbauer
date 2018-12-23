package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.GameState
import com.github.christophpickl.derbauer.logic.Player
import com.github.christophpickl.derbauer.logic.beepReturn
import com.github.christophpickl.derbauer.logic.decrement
import com.github.christophpickl.derbauer.logic.increment
import javax.inject.Inject
import kotlin.reflect.KMutableProperty1

class BuySellResourcesScreen(
    private val state: GameState
) : ChooseScreen<BuySellResourcesChoice> {

    override val message = "Gonna get cheap, huh?! What kind of resource you wanna trade mate?"

    override val choices
        get() = listOf(
            BuySellResourcesChoice(BuySellResourcesEnum.BuyLand, "Buy land (${state.prizes.landBuy}$)"),
            BuySellResourcesChoice(BuySellResourcesEnum.SellLand, "Sell land (${state.prizes.landSell}$)"),
            BuySellResourcesChoice(BuySellResourcesEnum.BuyFood, "Buy food (${state.prizes.foodBuy}$)"),
            BuySellResourcesChoice(BuySellResourcesEnum.SellFood, "Sell food (${state.prizes.foodSell}$)"),
            BuySellResourcesChoice(BuySellResourcesEnum.EndTurn, "End Turn")
        )

    override fun onCallback(callback: ScreenCallback) {
        callback.onBuySellResources(this)
    }

    enum class BuySellResourcesEnum {
        BuyLand,
        SellLand,
        BuyFood,
        SellFood,
        EndTurn
    }
}

class BuySellResourcesChoice(
    override val enum: Enum<BuySellResourcesScreen.BuySellResourcesEnum>,
    override val label: String
) : EnummedChoice<BuySellResourcesScreen.BuySellResourcesEnum>

class BuySellResourcesController @Inject constructor(
    private val state: GameState
) : ChooseScreenController<BuySellResourcesChoice, BuySellResourcesScreen> {

    override fun select(choice: BuySellResourcesChoice) {
        val nextScreen = when (choice.enum) {
            BuySellResourcesScreen.BuySellResourcesEnum.BuyLand -> LandBuyScreen(state)
            BuySellResourcesScreen.BuySellResourcesEnum.SellLand -> LandSellScreen(state)
            BuySellResourcesScreen.BuySellResourcesEnum.BuyFood -> FoodBuyScreen(state)
            BuySellResourcesScreen.BuySellResourcesEnum.SellFood -> FoodSellScreen(state)
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        state.screen = nextScreen
    }

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
        state.screen = HomeScreen(state)
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

class FoodBuyScreen(
    state: GameState
) : NumberInputScreen {
    override val message = "How much food do you wanna buy?\n" +
        "1 costs ${state.prizes.foodBuy} gold, you can afford ${state.affordableFood} food."

    override fun onCallback(callback: ScreenCallback) {
        callback.onFoodBuy(this)
    }
}

class FoodSellScreen(
    state: GameState
) : NumberInputScreen {
    override val message = "How much food do you wanna sell?\n1 for ${state.prizes.foodSell} gold, you've got ${state.player.food} food."
    override fun onCallback(callback: ScreenCallback) {
        callback.onFoodSell(this)
    }
}
