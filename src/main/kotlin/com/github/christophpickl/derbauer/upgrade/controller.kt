package com.github.christophpickl.derbauer.upgrade

import com.github.christophpickl.derbauer.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer.misc.validateChoice
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.AlertType
import mu.KotlinLogging.logger

class UpgradeController : UpgradeCallback {

    private val log = logger {}

    override fun doUpgrade(upgrade: Upgrade) {
        log.debug { "upgrade: $upgrade" }
        if (isValid(upgrade)) {
            Model.gold -= upgrade.buyPrice.rounded
            upgrade.currentLevel++
            require(upgrade.currentLevel <= upgrade.maxLevel)
            upgrade.execute()
            Model.currentView = UpgradeView()
        }
    }

    private fun isValid(upgrade: Upgrade) = validateChoice(listOf(
        SimpleChoiceValidation(
            condition = { !upgrade.isMaxLevelReached },
            alertType = AlertType.FullyUpgraded
        ),
        SimpleChoiceValidation(
            condition = { Model.gold >= upgrade.buyPrice.rounded },
            alertType = AlertType.NotEnoughGold
        )
    ))

}
