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
    choosePrompt = "Whom to attack today?",
    messages = Texts.militaryMessages,
    choices = mutableListOf<MilitaryChoice>(
        MilitaryChoice.Attack
    ).apply {
        addAll(Model.player.armies.all.map { MilitaryChoice.Hire(it) })
    },
    additionalContent = "You've got (used capacity " +
        "${Model.player.armies.totalAmount.formatted}/${Model.player.buildings.totalArmyCapacity.formatted}):\n" +
        Model.player.armies.all.joinToString("\n") {
            "  ${it.labelPlural.capitalize()}: ${it.amount.formatted}"
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

    class Hire(val army: Army) : MilitaryChoice() {
        override val label = formatLabel(army)
    }
}

interface MilitaryCallback {
    fun onMilitary(choice: MilitaryChoice)
    fun doHire(army: Army, amount: Long)
}


class HireView(
    private val army: Army
) : InputView(
    message = "How many ${army.labelPlural} do you wanna hire?\n\n" +
        "1 ${army.labelSingular} costs ${army.buyPrice.formatted} gold and ${army.costsPeople.formatted} people.\n\n" +
        "You can hire ${army.effectiveBuyPossibleAmount.formatted} ${army.labelPlural} maximum.",
    cancelSupport = CancelSupport.Enabled { MilitaryView() }
) {

    override fun onCallback(callback: ViewCallback, number: Long) {
        callback.doHire(army, number)
    }

}
