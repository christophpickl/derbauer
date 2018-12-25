package com.github.christophpickl.derbauer2.upgrade

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.home.HomeScreen
import com.github.christophpickl.derbauer2.model.Labeled
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.screen.CancelSupport
import com.github.christophpickl.derbauer2.ui.screen.Choice
import com.github.christophpickl.derbauer2.ui.screen.ChooseScreen

class UpgradeScreen : ChooseScreen<UpgradeChoice>(
    messages = listOf(
        "Bigger, stronger, faster! That's me.",
        "Always keep your stuff up2date.",
        "You want to play with the big boys, you first need big equipment."
    ),
    choices = Model.player.upgrades.all.map {
        UpgradeChoice(it)
    }
) {
    override val cancelSupport = CancelSupport.Enabled { HomeScreen() }
    override fun onCallback(callback: ScreenCallback, choice: UpgradeChoice) {
        callback.doUpgrade(choice)
    }
}

data class UpgradeChoice(
    val upgrade: Upgrade
) : Choice, Labeled {
    override val label =
        "${upgrade.label} ... ${upgrade.buyPrice} gold (${upgrade.description()})"
}

interface UpgradeCallback {
    fun doUpgrade(choice: UpgradeChoice)
}
