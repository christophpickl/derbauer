package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.HomeScreen
import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.ui.screen.CancelSupport
import com.github.christophpickl.derbauer2.ui.screen.Choice
import com.github.christophpickl.derbauer2.ui.screen.ChooseScreen

class BuildScreen : ChooseScreen<BuildChoice>(
    messages = listOf(
        "Expand, expand, expand.",
        "Construction work makes us happyyyy..."
    ),
    choices = BuildingTypes.all.map {
        BuildChoice(it)
    }
) {
    override val cancelSupport = CancelSupport.Enabled { HomeScreen() }
    override fun onCallback(callback: ScreenCallback, choice: BuildChoice) {
        callback.doBuild(choice)
    }
}

data class BuildChoice(
    val building: BuildingType
) : Choice {
    override val label: String = "Build ${building.labelSingular} ... ${building.buyPrice} $ and ${building.landNeeded} land"
}

interface BuildCallback {
    fun doBuild(choice: BuildChoice)
}
