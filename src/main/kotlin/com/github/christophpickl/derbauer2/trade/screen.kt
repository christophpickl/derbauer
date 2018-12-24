package com.github.christophpickl.derbauer2.trade

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.home.HomeScreen
import com.github.christophpickl.derbauer2.model.BuySell
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.TradeableResource
import com.github.christophpickl.derbauer2.ui.screen.CancelSupport
import com.github.christophpickl.derbauer2.ui.screen.Choice
import com.github.christophpickl.derbauer2.ui.screen.ChooseScreen
import com.github.christophpickl.derbauer2.ui.screen.InputScreen

class TradeScreen : ChooseScreen<TradableChoice>(
    messages = listOf(
        "Try not to get broke, huh?!",
        "Got anything useful?",
        "Psssst, over here! Looking for something?"
    ),
    choices = Model.player.resources.allTradeables.flatMap {
        listOf(TradableChoice(it, BuySell.Buy), TradableChoice(it, BuySell.Sell))
    }
) {
    override val cancelSupport = CancelSupport.Enabled { HomeScreen() }
    override fun onCallback(callback: ScreenCallback, choice: TradableChoice) {
        callback.onTrade(choice)
    }
}

data class TradableChoice(
    val resource: TradeableResource,
    val buySell: BuySell
) : Choice {
    override val label: String = "${buySell.label.capitalize()} ${resource.labelPlural} ... ${resource.effectivePriceFor(buySell)} $"
}

class ExecuteTradeScreen(private val choice: TradableChoice) : InputScreen(buildMessage(choice)) {
    companion object {
        private fun buildMessage(choice: TradableChoice): String {
            val info = when (choice.buySell) {
                BuySell.Buy -> "You can buy maximum: ${choice.resource.effectiveBuyPossible}"
                BuySell.Sell -> "You have ${choice.resource.sellPossible} available."
            }
            val verb = when (choice.buySell) {
                BuySell.Buy -> "costs"
                BuySell.Sell -> "brings"
            }
            return "How much ${choice.resource.labelPlural} do you wanna ${choice.buySell.label}?\n\n" +
                "1 ${choice.resource.labelSingular} $verb ${choice.resource.priceFor(choice.buySell)} gold.\n\n" +
                info
        }
    }

    override val cancelSupport = CancelSupport.Enabled { TradeScreen() }
    override fun onCallback(callback: ScreenCallback, number: Int) {
        callback.doTrade(choice, number)
    }
}

interface TradeCallback {
    fun onTrade(choice: TradableChoice)
    fun doTrade(choice: TradableChoice, amount: Int)
}
