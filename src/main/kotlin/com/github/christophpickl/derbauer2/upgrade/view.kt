package com.github.christophpickl.derbauer2.upgrade

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.data.Texts
import com.github.christophpickl.derbauer2.home.HomeView
import com.github.christophpickl.derbauer2.model.Labeled
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.view.CancelSupport
import com.github.christophpickl.derbauer2.ui.view.Choice
import com.github.christophpickl.derbauer2.ui.view.ChooseView

class UpgradeView : ChooseView<UpgradeChoice>(
    messages = Texts.upgradeMessages,
    choices = Model.player.upgrades.all.map {
        UpgradeChoice(it)
    }
) {
    override val cancelSupport = CancelSupport.Enabled { HomeView() }
    override fun onCallback(callback: ViewCallback, choice: UpgradeChoice) {
        callback.doUpgrade(choice)
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
    fun doUpgrade(choice: UpgradeChoice)
}
