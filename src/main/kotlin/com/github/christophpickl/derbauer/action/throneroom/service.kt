package com.github.christophpickl.derbauer.action.throneroom

import com.github.christophpickl.derbauer.action.throneroom.visitors.GeneralVisitor
import com.github.christophpickl.derbauer.action.throneroom.visitors.PoorBoyVisitor
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.view.YesNo
import com.github.christophpickl.derbauer.ui.view.YesNoCallback
import mu.KotlinLogging.logger

class ThroneRoomService : YesNoCallback {
    private val log = logger {}

    private val possibleVisitors: List<ThroneRoomVisitor<out ThroneRoomChoice>> = listOf(
        PoorBoyVisitor(),
        GeneralVisitor()
    )

    fun enter() {
        log.debug { "enter()" }
        val visitorsWaiting = Model.actions.visitorsWaitingInThroneRoom
        if (visitorsWaiting == 0) {
            Model.currentView = ThroneRoomEmptyView()
            return
        }
        Model.actions.visitorsWaitingInThroneRoom--

        val visitor = possibleVisitors.filter { it.condition() }.random()

        Model.currentView = ThroneRoomView(visitor)
    }

    fun <C : ThroneRoomChoice> choosen(visitor: ThroneRoomVisitor<C>, choice: C) {
        val response = visitor.choose(choice)
        if (Model.actions.visitorsWaitingInThroneRoom == 0) {
            Model.currentView = ThroneRoomEmptyView(response)
        } else {
            Model.currentView = ThroneRoomChoosenView(response, this)
        }
    }

    override fun onYesNo(choice: YesNo) {
        when (choice) {
            YesNo.Yes -> {
                enter()
            }
            YesNo.No -> {
                Model.goHome()
            }
        }
    }

}

interface ThroneRoomVisitor<C : ThroneRoomChoice> {
    val message: String
    val choices: List<C>
    fun condition(): Boolean
    fun choose(choice: C): String
}
