package com.github.christophpickl.derbauer.action

import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomChoice
import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomService

class ActionController : ActionCallback, OnSpecificAction {

    private val throneRoom = ThroneRoomService()

    override fun choiceSelected(choice: ActionChoice) {
        choice.action.onSpecificAction(this)
    }

    override fun onSpecificThroneRoomAction() {
        throneRoom.enter()
    }

    override fun <C : ThroneRoomChoice> onThroneRoomChoice(visitor: ThroneRoomVisitor<C>, choice: C) {
        throneRoom.choosen(visitor, choice)
    }

}
