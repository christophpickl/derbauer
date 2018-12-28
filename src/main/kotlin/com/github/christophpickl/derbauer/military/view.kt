package com.github.christophpickl.derbauer.military

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.data.Texts
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.Choice
import com.github.christophpickl.derbauer.ui.view.ChooseView
import com.github.christophpickl.derbauer.ui.view.InputView

class MilitaryView : ChooseView<MilitaryChoice>(
    messages = Texts.militaryMessages,
    choices = mutableListOf<MilitaryChoice>(
        MilitaryChoice.Attack
    ).apply {
        addAll(Model.player.militaries.all.map { MilitaryChoice.Hire(it) })
    },
    additionalContent = "You've got (used capacity " +
        "${Model.player.militaries.totalAmount}/${Model.player.buildings.totalMilitaryCapacity}):\n" +
        Model.player.militaries.all.joinToString("\n") {
            "  ${it.labelPlural.capitalize()}: ${it.amount}"
        },
    cancelSupport = CancelSupport.Enabled { HomeView() }
) {
    override fun onCallback(callback: ViewCallback, choice: MilitaryChoice) {
        callback.onMilitary(choice)
    }
}

sealed class MilitaryChoice : Choice {
    object Attack : MilitaryChoice() {
        override val label = "Attack"
    }

    class Hire(val military: Military) : MilitaryChoice() {
        override val label = formatLabel(military)
    }
}

interface MilitaryCallback {
    fun onMilitary(choice: MilitaryChoice)
    fun doHire(militaryUnit: Military, amount: Int)
}


class HireView(
    private val military: Military
) : InputView(
    message = "How many ${military.labelPlural} do you wanna hire?\n\n" +
        "1 ${military.labelSingular} costs ${military.buyPrice} gold and ${military.costsPeople} people.\n\n" +
        "You can hire ${military.effectiveBuyPossibleAmount} ${military.labelPlural} maximum.",
    cancelSupport = CancelSupport.Enabled { MilitaryView() }
) {

    override fun onCallback(callback: ViewCallback, number: Int) {
        callback.doHire(military, number)
    }

}
