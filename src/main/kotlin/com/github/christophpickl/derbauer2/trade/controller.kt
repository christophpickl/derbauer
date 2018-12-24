package com.github.christophpickl.derbauer2.trade

import com.github.christophpickl.derbauer2.model.BuySell
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.Alert
import com.github.christophpickl.derbauer2.ui.AlertType
import mu.KotlinLogging

class TradeController : TradeCallback {

    private val log = KotlinLogging.logger {}

    override fun onTrade(choice: TradableChoice) {
        Model.screen = ExecuteTradeScreen(choice)
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
            Model.gold += -1 * signator * totalPrice
            Model.goHome()
        }
    }

    private fun canTrade(choice: TradableChoice, amount: Int, totalPrice: Int): Boolean =
        when (choice.buySell) {
            BuySell.Buy -> {
                if (totalPrice > Model.gold) {
                    Alert.show(AlertType.NotEnoughGold)
                    false
                } else {
                    true
                }
            }
            BuySell.Sell -> {
                if (choice.resource.playerRead() < amount) {
                    Alert.show(AlertType.NotEnoughResourcesToSell)
                    false
                } else {
                    true
                }
            }
        }

}
