package com.github.christophpickl.derbauer.trade

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.buysell.BuySell
import com.github.christophpickl.derbauer.data.Texts
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.model.BuyAndSellableResource
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.Choice
import com.github.christophpickl.derbauer.ui.view.ChooseView
import com.github.christophpickl.derbauer.ui.view.InputView

class TradeView : ChooseView<TradeableChoice>(
    choosePrompt = "What do you want to trade?",
    messages = Texts.tradeMessages,
    choices = Model.player.resources.allTradeables.flatMap {
        listOf(TradeableChoice(it, BuySell.Buy), TradeableChoice(it, BuySell.Sell))
    },
    cancelSupport = CancelSupport.Enabled { HomeView() }
) {
    override fun onCallback(callback: ViewCallback, choice: TradeableChoice) {
        callback.onTrade(choice)
    }
}

data class TradeableChoice(
    val resource: BuyAndSellableResource,
    val buySell: BuySell
) : Choice {
    override val label = formatLabel(
        "${buySell.label.capitalize()} ${resource.labelPlural}",
        resource.descriptionFor(buySell)
    )
}

class ExecuteTradeView(
    private val choice: TradeableChoice
) : InputView(
    message = buildMessage(choice),
    cancelSupport = CancelSupport.Enabled { TradeView() }
) {
    companion object {
        private fun buildMessage(choice: TradeableChoice): String {
            val info = when (choice.buySell) {
                BuySell.Buy -> "You can buy maximum: ${choice.resource.effectiveBuyPossibleAmount.formatted}"
                BuySell.Sell -> "You have ${choice.resource.sellPossible.formatted} available."
            }
            val verb = when (choice.buySell) {
                BuySell.Buy -> "costs"
                BuySell.Sell -> "brings"
            }
            return "How much ${choice.resource.labelPlural} do you wanna ${choice.buySell.label}?\n\n" +
                "1 ${choice.resource.labelSingular} $verb ${choice.resource.priceFor(choice.buySell).formatted} gold.\n\n" +
                info
        }
    }

    override fun onCallback(callback: ViewCallback, number: Long) {
        callback.doTrade(choice, number)
    }
}

interface TradeCallback {
    fun onTrade(choice: TradeableChoice)
    fun doTrade(choice: TradeableChoice, amount: Long)
}
