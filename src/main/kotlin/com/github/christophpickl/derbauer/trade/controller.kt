package com.github.christophpickl.derbauer.trade

import com.github.christophpickl.derbauer.logic.ChooseScreenController
import com.github.christophpickl.derbauer.logic.beep
import com.github.christophpickl.derbauer.logic.decrement
import com.github.christophpickl.derbauer.logic.increment
import com.github.christophpickl.derbauer.misc.HomeScreen
import com.github.christophpickl.derbauer.model.Player
import com.github.christophpickl.derbauer.model.State
import kotlin.reflect.KMutableProperty1

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
