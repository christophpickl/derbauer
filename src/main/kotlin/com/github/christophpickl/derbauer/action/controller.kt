package com.github.christophpickl.derbauer.action

import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomChoice
import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomService
import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomVisitor

class ActionController : ActionCallback, OnSpecificAction {

    private val throneRoom = ThroneRoomService()

    override fun choiceSelected(choice: ActionChoice) {
        choice.action.onSpecificAction(this)
    }

    override fun onSpecificThroneRoomAction() {
        throneRoom.enter()
    }

    override fun onThroneRoomChoice(visitor: ThroneRoomVisitor, choice: ThroneRoomChoice) {
        throneRoom.choosen(visitor, choice)
    }

}
