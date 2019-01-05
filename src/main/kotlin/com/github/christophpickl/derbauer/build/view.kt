package com.github.christophpickl.derbauer.build

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.data.Texts
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.Choice
import com.github.christophpickl.derbauer.ui.view.ChooseView

class BuildView : ChooseView<BuildChoice>(
    choosePrompt = "What do you want to build?",
    messages = Texts.buildMessages,
    choices = Model.player.buildings.all.map {
        BuildChoice(it)
    },
    additionalContent = "You've got:\n${Model.player.buildings.all.joinToString("\n") {
        "  ${it.labelPlural.capitalize()}: ${it.amount.formatted}"
    }}",
    cancelSupport = CancelSupport.Enabled { HomeView() }
) {
    override fun onCallback(callback: ViewCallback, choice: BuildChoice) {
        callback.doBuild(choice)
    }
}

data class BuildChoice(
    val building: Building
) : Choice {
    override val label = formatLabel(building)
}

interface BuildCallback {
    fun doBuild(choice: BuildChoice)
}
