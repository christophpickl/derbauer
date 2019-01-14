package com.github.christophpickl.derbauer.military.attack.target

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.military.MilitaryView
import com.github.christophpickl.derbauer.model.Labeled
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.Choice
import com.github.christophpickl.derbauer.ui.view.ChooseView

class ChooseAttackTargetView : ChooseView<TargetChoice>(
    message = "Choose your attack target:",
    cancelSupport = CancelSupport.Enabled { MilitaryView() },
    choices = Model.military.targets.all.map { TargetChoice(it) }
) {

    override fun onCallback(callback: ViewCallback, choice: TargetChoice) {
        callback.onAttackTargetChosen(choice.target)
    }

}

data class TargetChoice(
    val target: AttackTarget
) : Choice, Labeled by target
