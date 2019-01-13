package com.github.christophpickl.derbauer.building

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.data.Messages
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.Choice
import com.github.christophpickl.derbauer.ui.view.ChooseView

class BuildView : ChooseView<BuildChoice>(
    message = Messages.build,
    choices = Model.player.buildings.all.map {
        BuildChoice(it)
    },
    cancelSupport = CancelSupport.Enabled { HomeView() }
) {
    override fun onCallback(callback: ViewCallback, choice: BuildChoice) {
        callback.doBuild(choice)
    }
}

data class BuildChoice(
    val building: Building
) : Choice {
    override val label = formatLabel(
        buyable = building,
        currentAmount = building.amount
    )
}

interface BuildCallback {
    fun doBuild(choice: BuildChoice)
}
