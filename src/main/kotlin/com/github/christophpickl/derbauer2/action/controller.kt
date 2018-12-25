package com.github.christophpickl.derbauer2.action

import com.github.christophpickl.derbauer2.action.throneRoom.ThroneRoomChoice
import com.github.christophpickl.derbauer2.action.throneRoom.ThroneRoomService
import com.github.christophpickl.derbauer2.action.throneRoom.ThroneRoomVisitor
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches

class ActionController : ActionCallback {

    private val throneRoom = ThroneRoomService()

    override fun onActionEnum(choice: ActionChoice) {
        when (choice.enum) {
            ActionEnum.ThroneRoom -> {
                throneRoom.enter()
            }
        }.enforceWhenBranches()
    }

    override fun onThroneRoomChoice(visitor: ThroneRoomVisitor, choice: ThroneRoomChoice) {
        throneRoom.choosen(visitor, choice)
    }

}
