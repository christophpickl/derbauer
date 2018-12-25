package com.github.christophpickl.derbauer2.action.throneRoom

import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.view.YesNo
import com.github.christophpickl.derbauer2.ui.view.YesNoCallback
import mu.KotlinLogging.logger

class ThroneRoomService : YesNoCallback {
    private val log = logger {}

    private val possibleVisitors: List<ThroneRoomVisitor> = listOf(
        PoorBoyVisitor(),
        GeneralDemandVisitor()
    )

    fun enter() {
        log.debug { "enter()" }
        val visitorsWaiting = Model.global.visitorsWaitingInThroneRoom
        if (visitorsWaiting == 0) {
            Model.currentView = ThroneRoomEmptyView()
            return
        }
        Model.global.visitorsWaitingInThroneRoom--

        val visitor = possibleVisitors.filter { it.condition() }.random()

        Model.currentView = ThroneRoomView(visitor)
    }

    fun choosen(visitor: ThroneRoomVisitor, choice: ThroneRoomChoice) {
        val response = visitor.choose(choice)
        if (Model.global.visitorsWaitingInThroneRoom == 0) {
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
