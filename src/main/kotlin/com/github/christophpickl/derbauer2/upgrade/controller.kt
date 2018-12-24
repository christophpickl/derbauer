package com.github.christophpickl.derbauer2.upgrade

import com.github.christophpickl.derbauer2.misc.ChoiceValidation
import com.github.christophpickl.derbauer2.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer2.misc.validateChoice
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.AlertType
import mu.KotlinLogging.logger

class UpgradeController : UpgradeCallback {

    private val log = logger {}

    override fun doUpgrade(choice: UpgradeChoice) {
        log.debug { "upgrade: $choice" }
        if (isValid(choice)) {
            Model.gold -= choice.upgrade.buyPrice
            choice.upgrade.execute()
            Model.goHome()
        }
    }

    private fun isValid(choice: UpgradeChoice): Boolean {
        val validations = mutableListOf<ChoiceValidation>()
        validations.add(SimpleChoiceValidation(
            condition = { Model.gold >= choice.upgrade.buyPrice },
            alertType = AlertType.NotEnoughGold
        ))
        return validateChoice(validations)
    }
}
