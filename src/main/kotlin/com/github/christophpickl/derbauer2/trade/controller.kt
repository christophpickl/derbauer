package com.github.christophpickl.derbauer2.trade

import com.github.christophpickl.derbauer2.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer2.misc.validateChoice
import com.github.christophpickl.derbauer2.model.BuySell
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.AlertType
import mu.KotlinLogging.logger

class TradeController : TradeCallback {

    private val log = logger {}

    override fun onTrade(choice: TradableChoice) {
        Model.screen = ExecuteTradeScreen(choice)
    }

    override fun doTrade(choice: TradableChoice, amount: Int) {
        log.debug { "doTrade(amount=$amount, choice=$choice)" }
        val resource = choice.resource
        val pricePerItem = resource.priceFor(choice.buySell)
        val totalPrice = pricePerItem * amount

        if (isValid(choice, amount, totalPrice)) {
            val signator = when (choice.buySell) {
                BuySell.Buy -> +1
                BuySell.Sell -> -1
            }
            resource.amount += signator * amount
            Model.gold += -1 * signator * totalPrice
            Model.goHome()
        }
    }

    private fun isValid(choice: TradableChoice, amount: Int, totalPrice: Int) =
        validateChoice(choice, listOf(
            SimpleChoiceValidation(
                condition = { it.buySell != BuySell.Buy || Model.gold >= totalPrice },
                alertType = AlertType.NotEnoughGold
            ),
            SimpleChoiceValidation(
                condition = { it.buySell != BuySell.Buy || Model.foodCapacityLeft >= amount },
                alertType = AlertType.NotEnoughCapacity
            ),
            SimpleChoiceValidation(
                condition = { it.buySell != BuySell.Sell || choice.resource.amount >= amount },
                alertType = AlertType.NotEnoughResourcesToSell
            )
        ))

}
