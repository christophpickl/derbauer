package com.github.christophpickl.derbauer.military

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.data.Messages
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.Choice
import com.github.christophpickl.derbauer.ui.view.ChooseView
import com.github.christophpickl.derbauer.ui.view.InputView

class MilitaryView : ChooseView<MilitaryChoice>(
    message = Messages.military,
    choices = mutableListOf<MilitaryChoice>(
        MilitaryChoice.Attack
    ).apply {
        addAll(Model.player.armies.all.map { MilitaryChoice.Hire(it) })
    },
    additionalContent = "Used capacity: " +
        "${Model.player.armies.totalAmount.formatted} / ${Model.player.buildings.totalArmyCapacity.formatted}",
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
        override val label = formatLabel(
            buyable = army,
            currentAmount = army.amount
        )
    }
}

interface MilitaryCallback {
    fun onMilitary(choice: MilitaryChoice)
    fun doHire(army: Army, amount: Long)
    fun executeAttack(chosenArmy: Map<Army, Amount>)
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
