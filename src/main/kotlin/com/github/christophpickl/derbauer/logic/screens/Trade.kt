package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.beepReturn
import com.github.christophpickl.derbauer.logic.decrement
import com.github.christophpickl.derbauer.logic.increment
import com.github.christophpickl.derbauer.model.Player
import com.github.christophpickl.derbauer.model.State
import javax.inject.Inject
import kotlin.reflect.KMutableProperty1

class TradeScreen(
    private val state: State
) : ChooseScreen<TradeChoice> {

    override val message = "Gonna get cheap, huh?! What kind of resource you wanna trade mate?"

    override val choices
        get() = listOf(
            TradeChoice(TradeEnum.BuyLand, "Buy land (${state.prices.landBuy}$)"),
            TradeChoice(TradeEnum.SellLand, "Sell land (${state.prices.landSell}$)"),
            TradeChoice(TradeEnum.BuyFood, "Buy food (${state.prices.foodBuy}$)"),
            TradeChoice(TradeEnum.SellFood, "Sell food (${state.prices.foodSell}$)")
        )

    override fun onCallback(callback: ScreenCallback) {
        callback.onTrade(this)
    }

}

enum class TradeEnum {
    BuyLand,
    SellLand,
    BuyFood,
    SellFood
}

class TradeChoice(
    override val enum: Enum<TradeEnum>,
    override val label: String
) : EnummedChoice<TradeEnum>

class TradeController @Inject constructor(
    private val state: State
) : ChooseScreenController<TradeChoice, TradeScreen> {

    override fun select(choice: TradeChoice) {
        val nextScreen = when (choice.enum) {
            TradeEnum.BuyLand -> LandBuyScreen(state)
            TradeEnum.SellLand -> LandSellScreen(state)
            TradeEnum.BuyFood -> FoodBuyScreen(state)
            TradeEnum.SellFood -> FoodSellScreen(state)
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        state.screen = nextScreen
    }

    fun buyLand(amount: Int) {
        buySellOperation(
            isBuying = true,
            amount = amount,
            costsPerItem = state.prices.landBuy,
            targetProperty = Player::land
        )
    }

    fun sellLand(amount: Int) {
        buySellOperation(
            isBuying = false,
            amount = amount,
            costsPerItem = state.prices.landSell,
            targetProperty = Player::land
        )
    }

    fun buyFood(amount: Int) {
        buySellOperation(
            isBuying = true,
            amount = amount,
            costsPerItem = state.prices.foodBuy,
            targetProperty = Player::food
        )
    }

    fun sellFood(amount: Int) {
        buySellOperation(
            isBuying = false,
            amount = amount,
            costsPerItem = state.prices.foodSell,
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
    state: State
) : NumberInputScreen {

    override val message = "How much land do you wanna buy?\n" +
        "1 costs ${state.prices.landBuy} gold, you can afford ${state.affordableLand} land."

    override fun onCallback(callback: ScreenCallback) {
        callback.onLandBuy(this)
    }
}

class LandSellScreen(
    state: State
) : NumberInputScreen {
    override val message = "How much land do you wanna sell?\n1 for ${state.prices.landSell} gold, you've got ${state.player.land} land."
    override fun onCallback(callback: ScreenCallback) {
        callback.onLandSell(this)
    }
}

class FoodBuyScreen(
    state: State
) : NumberInputScreen {
    override val message = "How much food do you wanna buy?\n" +
        "1 costs ${state.prices.foodBuy} gold, you can afford ${state.affordableFood} food."

    override fun onCallback(callback: ScreenCallback) {
        callback.onFoodBuy(this)
    }
}

class FoodSellScreen(
    state: State
) : NumberInputScreen {
    override val message = "How much food do you wanna sell?\n1 for ${state.prices.foodSell} gold, you've got ${state.player.food} food."
    override fun onCallback(callback: ScreenCallback) {
        callback.onFoodSell(this)
    }
}
