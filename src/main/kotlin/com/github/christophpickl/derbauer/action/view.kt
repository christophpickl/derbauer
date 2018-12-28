package com.github.christophpickl.derbauer.action

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomChoice
import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomVisitor
import com.github.christophpickl.derbauer.data.Texts
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.Choice
import com.github.christophpickl.derbauer.ui.view.ChooseView

class ActionView : ChooseView<ActionChoice>(
    messages = Texts.actionMessages,
    choices = Model.actions.all.map { ActionChoice(it) },
    cancelSupport = CancelSupport.Enabled { HomeView() }
) {
    override fun onCallback(callback: ViewCallback, choice: ActionChoice) {
        callback.choiceSelected(choice)
    }
}

data class ActionChoice(
    val action: Action
) : Choice {
    override val label = formatLabel(action.label.capitalize(), action.description)
}

interface ActionCallback {
    fun choiceSelected(choice: ActionChoice)
    fun onThroneRoomChoice(visitor: ThroneRoomVisitor, choice: ThroneRoomChoice) 
}
