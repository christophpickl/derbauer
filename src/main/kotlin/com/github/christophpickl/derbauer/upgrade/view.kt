package com.github.christophpickl.derbauer.upgrade

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.data.Messages
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.model.Labeled
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.Choice
import com.github.christophpickl.derbauer.ui.view.ChooseView

class UpgradeView : ChooseView<UpgradeChoice>(
    message = Messages.upgrade,
    choices = Model.player.upgrades.all.map {
        UpgradeChoice(it)
    },
    cancelSupport = CancelSupport.Enabled { HomeView() }
) {
    override fun onCallback(callback: ViewCallback, choice: UpgradeChoice) {
        callback.doUpgrade(choice.upgrade)
    }
}

data class UpgradeChoice(
    val upgrade: Upgrade
) : Choice, Labeled {
    override val label = formatLabel(
        "${upgrade.label} ${upgrade.currentLevel}/${upgrade.maxLevel}",
        "${upgrade.buyDescription} (${upgrade.description})"
    )
}

interface UpgradeCallback {
    fun doUpgrade(upgrade: Upgrade)
}
