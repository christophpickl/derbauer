package com.github.christophpickl.derbauer.action.throneroom

import com.github.christophpickl.derbauer.action.throneroom.visitors.GeneralVisitor
import com.github.christophpickl.derbauer.action.throneroom.visitors.PoorBoyVisitor
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.view.FeedbackView
import com.github.christophpickl.derbauer.ui.view.YesNo
import com.github.christophpickl.derbauer.ui.view.YesNoCallback
import mu.KotlinLogging.logger

class ThroneRoomController : YesNoCallback {

    private val log = logger {}
    private val throneRoom get () = Model.actions.throneRoom
    
    private val possibleVisitors: List<ThroneRoomVisitor<out ThroneRoomChoice>> = listOf(
        PoorBoyVisitor(),
        GeneralVisitor()
    )

    fun enter() {
        log.debug { "enter()" }
        val visitorsWaiting = throneRoom.visitorsWaiting
        if (visitorsWaiting == 0) {
            Model.currentView = ThroneRoomEmptyView()
            return
        }
        throneRoom.visitorsWaiting--

        val visitor = possibleVisitors.filter { it.condition() }.random()

        Model.currentView = ThroneRoomView(visitor)
    }

    fun <C : ThroneRoomChoice> choosen(visitor: ThroneRoomVisitor<C>, choice: C) {
        val response = visitor.choose(choice) ?: return
        Model.global.karma += choice.karmaEffect
        Model.currentView = FeedbackView(response) {
            if (throneRoom.visitorsWaiting == 0) {
                Model.currentView = ThroneRoomEmptyView()
            } else {
                Model.currentView = ThroneRoomChoosenView(this)
            }
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
