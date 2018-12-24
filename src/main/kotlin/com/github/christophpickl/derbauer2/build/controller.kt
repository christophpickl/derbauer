package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.misc.ChoiceValidation
import com.github.christophpickl.derbauer2.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer2.misc.validateChoice
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.AlertType

class BuildController : BuildCallback {

    private val validations = listOf<ChoiceValidation<BuildChoice>>(
        SimpleChoiceValidation(
            condition = { Model.gold >= it.building.buyPrice },
            alertType = AlertType.NotEnoughGold
        ),
        SimpleChoiceValidation(
            condition = { Model.availableLand >= it.building.landNeeded },
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

}
