package com.github.christophpickl.derbauer2.action

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.action.throneRoom.ThroneRoomChoice
import com.github.christophpickl.derbauer2.action.throneRoom.ThroneRoomVisitor
import com.github.christophpickl.derbauer2.home.HomeView
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.view.CancelSupport
import com.github.christophpickl.derbauer2.ui.view.Choice
import com.github.christophpickl.derbauer2.ui.view.ChooseView

class ActionView : ChooseView<ActionChoice>(
    messages = listOf(
        "Looking for some action, don't you?",
        "Sometimes, when I get bored, I cut my nails way too short..."
    ),
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
    override val label = "${action.label.capitalize()} ... ${action.description}"
}

interface ActionCallback {
    fun choiceSelected(choice: ActionChoice)
    fun onThroneRoomChoice(visitor: ThroneRoomVisitor, choice: ThroneRoomChoice) 
}
