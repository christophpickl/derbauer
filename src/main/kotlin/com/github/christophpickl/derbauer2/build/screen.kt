package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.home.HomeScreen
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.screen.CancelSupport
import com.github.christophpickl.derbauer2.ui.screen.Choice
import com.github.christophpickl.derbauer2.ui.screen.ChooseScreen

class BuildScreen : ChooseScreen<BuildChoice>(
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
    override val cancelSupport = CancelSupport.Enabled { HomeScreen() }
    override fun onCallback(callback: ScreenCallback, choice: BuildChoice) {
        callback.doBuild(choice)
    }
}

data class BuildChoice(
    val building: Building
) : Choice {
    override val label: String = "Build ${building.labelSingular} ... ${building.buyPrice} gold and ${building.landNeeded} land (${building.description})"
}

interface BuildCallback {
    fun doBuild(choice: BuildChoice)
}
