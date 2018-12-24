package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.misc.ChoiceValidation
import com.github.christophpickl.derbauer2.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer2.misc.validateChoice
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.AlertType

class BuildController : BuildCallback {

    private val validations = listOf<ChoiceValidation<BuildChoice>>(
        SimpleChoiceValidation(
            condition = { it.building.buyPrice > Model.gold },
            alertType = AlertType.NotEnoughGold
        ),
        SimpleChoiceValidation(
            condition = { it.building.landNeeded > Model.availableLand },
            alertType = AlertType.NotEnoughLand
        )
    )

    override fun doBuild(choice: BuildChoice) {
        if (validateChoice(validations, choice)) {
            choice.building.playerChange(+1)
            Model.land -= choice.building.landNeeded
            Model.gold -= choice.building.buyPrice
            Model.goHome()
        }
    }

    /* 
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
            
        }
    }
     */
}
