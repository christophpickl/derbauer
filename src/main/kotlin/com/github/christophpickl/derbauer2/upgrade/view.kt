package com.github.christophpickl.derbauer2.upgrade

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.home.HomeView
import com.github.christophpickl.derbauer2.model.Labeled
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.view.CancelSupport
import com.github.christophpickl.derbauer2.ui.view.Choice
import com.github.christophpickl.derbauer2.ui.view.ChooseView

class UpgradeView : ChooseView<UpgradeChoice>(
    messages = listOf(
        "Bigger, stronger, faster! That's me.",
        "Always keep your stuff up2date.",
        "You want to play with the big boys, you first need big equipment."
    ),
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
    override val label =
        "${upgrade.label} ${upgrade.currentLevel}/${upgrade.maxLevel} ... ${upgrade.buyDescription} (${upgrade.description})"
}

interface UpgradeCallback {
    fun doUpgrade(choice: UpgradeChoice)
}
