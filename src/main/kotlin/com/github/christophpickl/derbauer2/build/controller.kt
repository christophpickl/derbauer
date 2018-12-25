package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer2.misc.validateChoice
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.AlertType
import mu.KotlinLogging.logger

class BuildController : BuildCallback {

    private val log = logger {}
    
    override fun doBuild(choice: BuildChoice) {
        log.debug { "try building: $choice" }
        if (isValid(choice)) {
            choice.building.amount++
            Model.gold -= choice.building.buyPrice
            Model.goHome()
        }
    }

    private fun isValid(choice: BuildChoice) = validateChoice(listOf(
        SimpleChoiceValidation(
            condition = { Model.gold >= choice.building.buyPrice },
            alertType = AlertType.NotEnoughGold
        ),
        SimpleChoiceValidation(
            condition = { Model.player.resources.land.unusedAmount >= choice.building.landNeeded },
            alertType = AlertType.NotEnoughLand
        )
    ))

}
