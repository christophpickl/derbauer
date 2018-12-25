package com.github.christophpickl.derbauer2.action

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.action.throneRoom.ThroneRoomChoice
import com.github.christophpickl.derbauer2.action.throneRoom.ThroneRoomVisitor
import com.github.christophpickl.derbauer2.home.HomeView
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.view.CancelSupport
import com.github.christophpickl.derbauer2.ui.view.ChooseView
import com.github.christophpickl.derbauer2.ui.view.EnumChoice

class ActionView : ChooseView<ActionChoice>(
    messages = listOf(
        "Looking for some action, don't you?"
    ),
    choices = listOf(
        EnumChoice(ActionEnum.ThroneRoom, "Go to throne room (${Model.global.visitorsWaitingInThroneRoom} visitor${if (Model.global.visitorsWaitingInThroneRoom == 1) "" else "s"} waiting)")
    )
) {
    override val cancelSupport = CancelSupport.Enabled { HomeView() }
    override fun onCallback(callback: ViewCallback, choice: ActionChoice) {
        callback.onActionEnum(choice)
    }
}

typealias ActionChoice = EnumChoice<ActionEnum>

enum class ActionEnum {
    ThroneRoom
}

interface ActionCallback {
    fun onActionEnum(choice: ActionChoice)
    fun onThroneRoomChoice(visitor: ThroneRoomVisitor, choice: ThroneRoomChoice)
}
