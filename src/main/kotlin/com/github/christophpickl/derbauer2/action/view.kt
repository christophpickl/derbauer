package com.github.christophpickl.derbauer2.action

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.action.throneRoom.ThroneRoomChoice
import com.github.christophpickl.derbauer2.action.throneRoom.ThroneRoomVisitor
import com.github.christophpickl.derbauer2.data.Texts
import com.github.christophpickl.derbauer2.home.HomeView
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.view.CancelSupport
import com.github.christophpickl.derbauer2.ui.view.Choice
import com.github.christophpickl.derbauer2.ui.view.ChooseView

class ActionView : ChooseView<ActionChoice>(
    messages = Texts.actionMessages,
    choices = Model.actions.all.map { ActionChoice(it) }
) {
    override val cancelSupport = CancelSupport.Enabled { HomeView() }
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
