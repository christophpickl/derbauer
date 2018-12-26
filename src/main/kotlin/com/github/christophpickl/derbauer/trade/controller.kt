package com.github.christophpickl.derbauer.trade

import com.github.christophpickl.derbauer.misc.ChoiceValidation
import com.github.christophpickl.derbauer.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer.misc.validateChoice
import com.github.christophpickl.derbauer.model.LimitedAmount
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.UsableEntity
import com.github.christophpickl.derbauer.ui.AlertType
import mu.KotlinLogging.logger

class TradeController : TradeCallback {

    private val log = logger {}

    override fun onTrade(choice: TradeableChoice) {
        Model.currentView = ExecuteTradeView(choice)
    }

    override fun doTrade(choice: TradeableChoice, amount: Int) {
        log.debug { "doTrade(amount=$amount, choice=$choice)" }
        val resource = choice.resource
        val pricePerItem = resource.effectivePriceFor(choice.buySell)
        val totalPrice = pricePerItem * amount

        if (isValid(choice, amount, totalPrice)) {
            val signator = when (choice.buySell) {
                BuySell.Buy -> +1
                BuySell.Sell -> -1
            }
            resource.amount += signator * amount
            Model.gold += -1 * signator * totalPrice
            Model.history.traded++
            Model.currentView = TradeView()
        }
    }

    private fun isValid(choice: TradeableChoice, amount: Int, totalPrice: Int): Boolean {
        val validations = mutableListOf<ChoiceValidation>()
        if (choice.buySell == BuySell.Buy) {
            // enough gold
            validations.add(SimpleChoiceValidation(
                condition = { Model.gold >= totalPrice },
                alertType = AlertType.NotEnoughGold
            ))
            // within capacity limit
            if (choice.resource is LimitedAmount) {
                validations.add(SimpleChoiceValidation(
                    condition = { choice.resource.capacityLeft >= amount },
                    alertType = AlertType.NotEnoughCapacity
                ))
            }

        } else {
            // enough amount
            validations.add(SimpleChoiceValidation(
                condition = { choice.resource.amount >= amount },
                alertType = AlertType.NotEnoughResourcesToSell
            ))
            // unused amount (for land)
            if (choice.resource is UsableEntity) {
                validations.add(SimpleChoiceValidation(
                    condition = { choice.resource.unusedAmount >= amount },
                    alertType = AlertType.NotEnoughResources
                ))
            }
        }
        return validateChoice(validations)
    }
}