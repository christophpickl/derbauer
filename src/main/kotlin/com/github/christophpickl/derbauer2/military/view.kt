package com.github.christophpickl.derbauer2.military

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.home.HomeView
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.view.CancelSupport
import com.github.christophpickl.derbauer2.ui.view.Choice
import com.github.christophpickl.derbauer2.ui.view.ChooseView
import com.github.christophpickl.derbauer2.ui.view.InputView

class MilitaryView : ChooseView<MilitaryChoice>(
    messages = listOf(
        "Hey, don't look at me dude.",
        "Gonna kick some ass!",
        "Any problem can be solved with brute force and violence."
    ),
    choices = mutableListOf<MilitaryChoice>(
        MilitaryChoice.Attack
    ).apply {
        addAll(Model.player.militaries.all.map { MilitaryChoice.Hire(it) })
    },
    additionalContent = "You've got (used capacity ${Model.player.militaries.totalAmount}/${Model.player.buildings.totalMilitaryCapacity}):\n" +
        Model.player.militaries.all.joinToString("\n") {
            "  ${it.labelPlural.capitalize()}: ${it.amount}"
        }
) {
    override val cancelSupport = CancelSupport.Enabled { HomeView() }
    override fun onCallback(callback: ViewCallback, choice: MilitaryChoice) {
        callback.onMilitary(choice)
    }
}

sealed class MilitaryChoice(
    override val label: String
) : Choice {
    object Attack : MilitaryChoice("Attack")
    class Hire(val military: Military) : MilitaryChoice(
        label = "Hire ${military.label} ... ${military.buyDescription} (${military.description})"
    )
}

interface MilitaryCallback {
    fun onMilitary(choice: MilitaryChoice)
    fun doHire(militaryUnit: Military, amount: Int)
}


class HireView(
    private val military: Military
) : InputView(
    "How many ${military.labelPlural} do you wanna hire?\n\n" +
        "1 ${military.labelSingular} costs ${military.buyPrice} gold and ${military.costsPeople} people.\n\n" +
        "You can hire ${military.effectiveBuyPossibleAmount} ${military.labelPlural} maximum."
) {

    override fun onCallback(callback: ViewCallback, number: Int) {
        callback.doHire(military, number)
    }

    override val cancelSupport = CancelSupport.Enabled { MilitaryView() }
}
