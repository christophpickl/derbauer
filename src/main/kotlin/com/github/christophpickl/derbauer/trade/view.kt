package com.github.christophpickl.derbauer.trade

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.data.Texts
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.TradeableResource
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.Choice
import com.github.christophpickl.derbauer.ui.view.ChooseView
import com.github.christophpickl.derbauer.ui.view.InputView

class TradeView : ChooseView<TradeableChoice>(
    messages = Texts.tradeMessages,
    choices = Model.player.resources.allTradeables.flatMap {
        listOf(TradeableChoice(it, BuySell.Buy), TradeableChoice(it, BuySell.Sell))
    }
) {
    override val cancelSupport = CancelSupport.Enabled { HomeView() }
    override fun onCallback(callback: ViewCallback, choice: TradeableChoice) {
        callback.onTrade(choice)
    }
}

data class TradeableChoice(
    val resource: TradeableResource,
    val buySell: BuySell
) : Choice {
    override val label = formatLabel(
        "${buySell.label.capitalize()} ${resource.labelPlural}",
        resource.descriptionFor(buySell)
    )
}

class ExecuteTradeView(private val choice: TradeableChoice) : InputView(buildMessage(choice)) {
    companion object {
        private fun buildMessage(choice: TradeableChoice): String {
            val info = when (choice.buySell) {
                BuySell.Buy -> "You can buy maximum: ${choice.resource.effectiveBuyPossibleAmount}"
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

    override val cancelSupport = CancelSupport.Enabled { TradeView() }
    override fun onCallback(callback: ViewCallback, number: Int) {
        callback.doTrade(choice, number)
    }
}

interface TradeCallback {
    fun onTrade(choice: TradeableChoice)
    fun doTrade(choice: TradeableChoice, amount: Int)
}
