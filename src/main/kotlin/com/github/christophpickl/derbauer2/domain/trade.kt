package com.github.christophpickl.derbauer2.domain

import com.github.christophpickl.derbauer2.misc.CancelSupport
import com.github.christophpickl.derbauer2.misc.Choice
import com.github.christophpickl.derbauer2.misc.ChooseScreen
import com.github.christophpickl.derbauer2.misc.InputScreen
import com.github.christophpickl.derbauer2.misc.ScreenCallback
import com.github.christophpickl.derbauer2.state.BuySell
import com.github.christophpickl.derbauer2.state.ResourceTypes
import com.github.christophpickl.derbauer2.state.State
import com.github.christophpickl.derbauer2.state.TradeableResourceType
import com.github.christophpickl.derbauer2.ui.Alert
import com.github.christophpickl.derbauer2.ui.AlertType
import mu.KotlinLogging.logger

class TradeScreen : ChooseScreen<TradableChoice>(
    messages = listOf(
        "Try not to get broke, huh?!",
        "Got anything useful?",
        "Psssst, over here! Looking for something?"
    ),
    choices = ResourceTypes.tradeables.flatMap {
        listOf(TradableChoice(it, BuySell.Buy), TradableChoice(it, BuySell.Sell))
    }
) {
    override val cancelSupport = CancelSupport.Enabled { HomeScreen() }
    override fun onCallback(callback: ScreenCallback, choice: TradableChoice) {
        callback.onTrade(choice)
    }
}

data class TradableChoice(
    val resource: TradeableResourceType,
    val buySell: BuySell
) : Choice {
    override val label: String = "${buySell.labelCapital} ${resource.labelPlural} ... ${resource.priceFor(buySell)} $"
}

class ExecuteTradeScreen(private val choice: TradableChoice) : InputScreen(buildMessage(choice)) {
    companion object {
        private fun buildMessage(choice: TradableChoice): String {
            val info = when (choice.buySell) {
                BuySell.Buy -> "You can afford at maximum: ${choice.resource.buyPossible}"
                BuySell.Sell -> "You have ${choice.resource.sellPossible} available."
            }
            val verb = when (choice.buySell) {
                BuySell.Buy -> "costs"
                BuySell.Sell -> "brings"
            }
            return "How much ${choice.resource.labelPlural} do you wanna ${choice.buySell.labelSmall}?\n\n" +
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
    fun doTrade(choice: TradableChoice, amount: Int)
    fun onTrade(choice: TradableChoice)
}

class TradeController : TradeCallback {

    private val log = logger {}
    
    override fun onTrade(choice: TradableChoice) {
        State.screen = ExecuteTradeScreen(choice)
    }

    override fun doTrade(choice: TradableChoice, amount: Int) {
        log.debug { "doTrade(amount=$amount, choice=$choice)" }
        val resource = choice.resource
        val pricePerItem = resource.priceFor(choice.buySell)
        val totalPrice = pricePerItem * amount

        if (canTrade(choice, amount, totalPrice)) {
            val signator = when (choice.buySell) {
                BuySell.Buy -> +1
                BuySell.Sell -> -1
            }
            resource.playerChange(signator * amount)
            State.gold += -1 * signator * totalPrice
            State.goHome()
        }
    }

    private fun canTrade(choice: TradableChoice, amount: Int, totalPrice: Int): Boolean =
        when (choice.buySell) {
            BuySell.Buy -> {
                if (totalPrice > State.gold) {
                    Alert.show(AlertType.NotEnoughGold)
                    false
                } else {
                    true
                }
            }
            BuySell.Sell -> {
                if (choice.resource.playerRead().amount < amount) {
                    Alert.show(AlertType.NotEnoughResourcesToSell)
                    false
                } else {
                    true
                }
            }
        }

}
