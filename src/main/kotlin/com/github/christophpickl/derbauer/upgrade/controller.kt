package com.github.christophpickl.derbauer.upgrade

import com.github.christophpickl.derbauer.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer.misc.validateChoice
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.AlertType
import mu.KotlinLogging.logger

class UpgradeController : UpgradeCallback {

    private val log = logger {}

    override fun doUpgrade(choice: UpgradeChoice) {
        log.debug { "upgrade: $choice" }
        if (isValid(choice)) {
            Model.gold -= choice.upgrade.buyPrice
            choice.upgrade.currentLevel++
            choice.upgrade.execute()
            Model.currentView = UpgradeView()
        }
    }

    private fun isValid(choice: UpgradeChoice) = validateChoice(listOf(
        SimpleChoiceValidation(
            condition = { !choice.upgrade.isMaxLevelReached },
            alertType = AlertType.FullyUpgraded
        ),
        SimpleChoiceValidation(
            condition = { Model.gold >= choice.upgrade.buyPrice },
            alertType = AlertType.NotEnoughGold
        )
    ))

}
