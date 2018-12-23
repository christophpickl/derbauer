package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.beep
import com.github.christophpickl.derbauer.logic.decrement
import com.github.christophpickl.derbauer.logic.formatNumber
import com.github.christophpickl.derbauer.logic.increment
import com.github.christophpickl.derbauer.model.Player
import com.github.christophpickl.derbauer.model.State
import javax.inject.Inject
import kotlin.reflect.KMutableProperty1

class TradeScreen(
    private val state: State
) : ChooseScreen<TradeChoice> {

    private val messages = listOf(
        "Try not to get broke, huh?!",
        "Got anything useful?",
        "Psssst, over here! Looking for something?"
    )
    override val message = messages.random()

    //@formatter:off
    override val choices
        get() = listOf(
            TradeChoice(TradeEnum.BuyLand,  "Buy land  ... ${formatNumber(state.prices.trade.landBuy, 2)} $"),
            TradeChoice(TradeEnum.SellLand, "Sell land ... ${formatNumber(state.prices.trade.landSell, 2)} $"),
            TradeChoice(TradeEnum.BuyFood,  "Buy food  ... ${formatNumber(state.prices.trade.foodBuy, 2)} $"),
            TradeChoice(TradeEnum.SellFood, "Sell food ... ${formatNumber(state.prices.trade.foodSell, 2)} $")
        )
    //@formatter:on

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
        trade(
            isBuying = true,
            amount = amount,
            costsPerItem = state.prices.trade.landBuy,
            targetProperty = Player::land
        )
    }

    fun sellLand(amount: Int) {
        trade(
            isBuying = false,
            amount = amount,
            costsPerItem = state.prices.trade.landSell,
            targetProperty = Player::land,
            additionalCheck = { state.player.landAvailable >= amount }
        )
    }

    fun buyFood(amount: Int) {
        trade(
            isBuying = true,
            amount = amount,
            costsPerItem = state.prices.trade.foodBuy,
            targetProperty = Player::food
        )
    }

    fun sellFood(amount: Int) {
        trade(
            isBuying = false,
            amount = amount,
            costsPerItem = state.prices.trade.foodSell,
            targetProperty = Player::food
        )
    }

    private fun trade(
        isBuying: Boolean,
        amount: Int,
        targetProperty: KMutableProperty1<Player, Int>,
        costsPerItem: Int,
        additionalCheck: () -> Boolean = { true }
    ) {
        if (!additionalCheck()) return beep()
        val goldChanging = if (isBuying) {
            val costs = canAffordCalcCosts(costsPerItem, amount) ?: return beep()
            targetProperty.increment(state.player, amount)
            costs
        } else {
            val income = hasEnoughCalcIncome(costsPerItem, amount, targetProperty.get(state.player)) ?: return beep()
            targetProperty.decrement(state.player, amount)
            income
        }
        state.history.traded++
        state.player.gold += if (isBuying) (-1 * goldChanging) else goldChanging
        state.screen = HomeScreen(state)
    }

    private fun canAffordCalcCosts(costsPerItem: Int, amount: Int): Int? {
        val costs = costsPerItem * amount
        if (costs > state.player.gold) {
            return null
        }
        return costs
    }

    private fun hasEnoughCalcIncome(costsPerItem: Int, amount: Int, has: Int): Int? {
        if (has < amount) {
            return null
        }
        return costsPerItem * amount
    }

}

class LandBuyScreen(
    state: State
) : NumberInputScreen {

    override val message = "How much land do you wanna buy?\n\n" +
        "1 costs ${state.prices.trade.landBuy} gold, you can afford ${state.affordableLand} land."

    override fun onCallback(callback: ScreenCallback) {
        callback.onLandBuy(this)
    }
}

class LandSellScreen(
    state: State
) : NumberInputScreen {
    override val message = "How much land do you wanna sell?\n\n" +
        "1 for ${state.prices.trade.landSell} gold, you've got ${state.player.landAvailable} land available."
    override fun onCallback(callback: ScreenCallback) {
        callback.onLandSell(this)
    }
}

class FoodBuyScreen(
    state: State
) : NumberInputScreen {
    override val message = "How much food do you wanna buy?\n\n" +
        "1 costs ${state.prices.trade.foodBuy} gold, you can afford ${state.affordableFood} food."

    override fun onCallback(callback: ScreenCallback) {
        callback.onFoodBuy(this)
    }
}

class FoodSellScreen(
    state: State
) : NumberInputScreen {
    override val message = "How much food do you wanna sell?\n\n" +
        "1 for ${state.prices.trade.foodSell} gold, you've got ${state.player.food} food."
    override fun onCallback(callback: ScreenCallback) {
        callback.onFoodSell(this)
    }
}

val State.affordableLand get() = player.gold / prices.trade.landBuy
val State.affordableFood get() = player.gold / prices.trade.foodBuy
