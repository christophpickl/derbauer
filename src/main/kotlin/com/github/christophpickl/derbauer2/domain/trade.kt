package com.github.christophpickl.derbauer2.domain

import com.github.christophpickl.derbauer2.ResourceTypes
import com.github.christophpickl.derbauer2.State
import com.github.christophpickl.derbauer2.TradeOption
import com.github.christophpickl.derbauer2.TradeableResourceType
import com.github.christophpickl.derbauer2.misc.CancelSupport
import com.github.christophpickl.derbauer2.misc.Choice
import com.github.christophpickl.derbauer2.misc.ChooseScreen
import com.github.christophpickl.derbauer2.misc.InputScreen
import com.github.christophpickl.derbauer2.misc.ScreenCallback
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches

class TradeScreen : ChooseScreen<TradableChoice>(
    message = "What do you wanna trade?",
    choices = ResourceTypes.tradeables.flatMap {
        listOf(TradableChoice(it, TradeOption.Buy), TradableChoice(it, TradeOption.Sell))
    }
) {
    override val cancelSupport = CancelSupport.Enabled { HomeScreen() }
    override fun onCallback(callback: ScreenCallback, choice: TradableChoice) {
        callback.onTrade(choice)
    }
}

class TradableChoice(
    val type: TradeableResourceType,
    val tradeOption: TradeOption
) : Choice {
    override val label: String = "${tradeOption.label} ${type.labelPlural} ... ${type.priceFor(tradeOption)} $"
}

class ExecuteTradeScreen(private val choice: TradableChoice) : InputScreen(
    "How much ${choice.type.labelPlural} do you wanna ${choice.tradeOption.label}?"
) {
    override val cancelSupport = CancelSupport.Enabled { TradeScreen() }
    override fun onCallback(callback: ScreenCallback, number: Int) {
        callback.doTrade(choice, number)
    }
}

interface TradeCallback {
    fun doTrade(choice: TradableChoice, amount: Int)
    fun onTrade(choice: TradableChoice)
}

class TradeController : TradeCallback {
    override fun onTrade(choice: TradableChoice) {
        State.screen = ExecuteTradeScreen(choice)
    }

    override fun doTrade(choice: TradableChoice, amount: Int) {
        val playerValue = choice.type.playerValue()
        when (choice.tradeOption) {
            TradeOption.Buy -> {

            }
            TradeOption.Sell -> {

            }
        }.enforceWhenBranches()
        State.goHome()
    }
//    
//    override fun onBuyFood() {
//        State.screen = BuyFoodScreen()
//    }

//    override fun doBuyFood(amount: Int) {
//        if (Random.nextBoolean()) {
//            println("food bought: $amount")
//            State.player.resources.food += amount
//            State.player.resources.gold -= amount * 2
//            State.goHome()
//        } else {
//            println("fail")
//            beep()
//        }
//    }

}
