package com.github.christophpickl.derbauer.building

import com.github.christophpickl.derbauer.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer.misc.validateChoice
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.AlertType
import mu.KotlinLogging.logger

class BuildController : BuildCallback {

    private val log = logger {}

    override fun doBuild(choice: BuildChoice): Boolean {
        log.debug { "try building: $choice" }
        return if (isValid(choice)) {
            choice.building.amount++
            Model.gold -= choice.building.buyPrice.rounded
            Model.currentView = BuildView()
            true
        } else false
    }

    private fun isValid(choice: BuildChoice) = validateChoice(listOf(
        SimpleChoiceValidation(
            condition = { Model.gold >= choice.building.buyPrice.rounded },
            alertType = AlertType.NotEnoughGold
        ),
        SimpleChoiceValidation(
            condition = { Model.player.resources.land.unusedAmount >= choice.building.landNeeded },
            alertType = AlertType.NotEnoughLand
        )
    ))

}
