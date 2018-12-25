package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.home.HomeView
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.view.CancelSupport
import com.github.christophpickl.derbauer2.ui.view.Choice
import com.github.christophpickl.derbauer2.ui.view.ChooseView

class BuildView : ChooseView<BuildChoice>(
    messages = listOf(
        "Expand, expand, expand.",
        "Construction work makes us happyyyy..."
    ),
    choices = Model.player.buildings.all.map {
        BuildChoice(it)
    },
    additionalContent = "You've got:\n${Model.player.buildings.all.joinToString("\n") {
        "  ${it.labelPlural.capitalize()}: ${it.amount}"
    }}"
) {
    override val cancelSupport = CancelSupport.Enabled { HomeView() }
    override fun onCallback(callback: ViewCallback, choice: BuildChoice) {
        callback.doBuild(choice)
    }
}

data class BuildChoice(
    val building: Building
) : Choice {
    override val label =
        "${building.labelSingular} ... ${building.buyDescription()} (${building.description()})"
}

interface BuildCallback {
    fun doBuild(choice: BuildChoice)
}
