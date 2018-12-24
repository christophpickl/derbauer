package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer2.misc.validateChoice
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.AlertType

class BuildController : BuildCallback {

    override fun doBuild(choice: BuildChoice) {
        if (isValid(choice)) {
            choice.building.amount++
            Model.gold -= choice.building.buyPrice
            Model.goHome()
        }
    }

    private fun isValid(choice: BuildChoice) = validateChoice(choice, listOf(
        SimpleChoiceValidation(
            condition = { Model.gold >= it.building.buyPrice },
            alertType = AlertType.NotEnoughGold
        ),
        SimpleChoiceValidation(
            condition = { Model.landUnused >= it.building.landNeeded },
            alertType = AlertType.NotEnoughLand
        )
    ))

}
