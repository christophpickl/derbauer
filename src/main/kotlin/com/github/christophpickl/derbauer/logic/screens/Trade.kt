package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.beep
import com.github.christophpickl.derbauer.logic.decrement
import com.github.christophpickl.derbauer.logic.formatNumber
import com.github.christophpickl.derbauer.logic.increment
import com.github.christophpickl.derbauer.model.Player
import com.github.christophpickl.derbauer.model.State
import kotlin.reflect.KMutableProperty1

class TradeScreen : ChooseScreen<TradeChoice> {

    private val messages = listOf(
        "Try not to get broke, huh?!",
        "Got anything useful?",
        "Psssst, over here! Looking for something?"
    )
    override val message = messages.random()

    //@formatter:off
    override val choices
        get() = listOf(
            TradeChoice(TradeEnum.BuyLand,  "Buy land  ... ${formatNumber(State.prices.trade.landBuy, 2)} $"),
            TradeChoice(TradeEnum.SellLand, "Sell land ... ${formatNumber(State.prices.trade.landSell, 2)} $"),
            TradeChoice(TradeEnum.BuyFood,  "Buy food  ... ${formatNumber(State.prices.trade.foodBuy, 2)} $"),
            TradeChoice(TradeEnum.SellFood, "Sell food ... ${formatNumber(State.prices.trade.foodSell, 2)} $")
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

class TradeController : ChooseScreenController<TradeChoice, TradeScreen> {

    override fun select(choice: TradeChoice) {
        val nextScreen = when (choice.enum) {
            TradeEnum.BuyLand -> LandBuyScreen()
            TradeEnum.SellLand -> LandSellScreen()
            TradeEnum.BuyFood -> FoodBuyScreen()
            TradeEnum.SellFood -> FoodSellScreen()
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        State.screen = nextScreen
    }

    fun buyLand(amount: Int) {
        trade(
            isBuying = true,
            amount = amount,
            costsPerItem = State.prices.trade.landBuy,
            targetProperty = Player::land
        )
    }

    fun sellLand(amount: Int) {
        trade(
            isBuying = false,
            amount = amount,
            costsPerItem = State.prices.trade.landSell,
            targetProperty = Player::land,
            additionalCheck = { State.player.landAvailable >= amount }
        )
    }

    fun buyFood(amount: Int) {
        trade(
            isBuying = true,
            amount = amount,
            costsPerItem = State.prices.trade.foodBuy,
            targetProperty = Player::food
        )
    }

    fun sellFood(amount: Int) {
        trade(
            isBuying = false,
            amount = amount,
            costsPerItem = State.prices.trade.foodSell,
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
            targetProperty.increment(State.player, amount)
            costs
        } else {
            val income = hasEnoughCalcIncome(costsPerItem, amount, targetProperty.get(State.player)) ?: return beep()
            targetProperty.decrement(State.player, amount)
            income
        }
        State.history.traded++
        State.player.gold += if (isBuying) (-1 * goldChanging) else goldChanging
        State.screen = HomeScreen()
    }

    private fun canAffordCalcCosts(costsPerItem: Int, amount: Int): Int? {
        val costs = costsPerItem * amount
        if (costs > State.player.gold) {
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

class LandBuyScreen : NumberInputScreen {

    override val message = "How much land do you wanna buy?\n\n" +
        "1 costs ${State.prices.trade.landBuy} gold, you can afford ${State.affordableLand} land."

    override fun onCallback(callback: ScreenCallback) {
        callback.onLandBuy(this)
    }
}

class LandSellScreen : NumberInputScreen {
    override val message = "How much land do you wanna sell?\n\n" +
        "1 for ${State.prices.trade.landSell} gold, you've got ${State.player.landAvailable} land available."
    override fun onCallback(callback: ScreenCallback) {
        callback.onLandSell(this)
    }
}

class FoodBuyScreen : NumberInputScreen {
    override val message = "How much food do you wanna buy?\n\n" +
        "1 costs ${State.prices.trade.foodBuy} gold, you can afford ${State.affordableFood} food."

    override fun onCallback(callback: ScreenCallback) {
        callback.onFoodBuy(this)
    }
}

class FoodSellScreen : NumberInputScreen {
    override val message = "How much food do you wanna sell?\n\n" +
        "1 for ${State.prices.trade.foodSell} gold, you've got ${State.player.food} food."
    override fun onCallback(callback: ScreenCallback) {
        callback.onFoodSell(this)
    }
}

val State.affordableLand get() = player.gold / prices.trade.landBuy
val State.affordableFood get() = player.gold / prices.trade.foodBuy
