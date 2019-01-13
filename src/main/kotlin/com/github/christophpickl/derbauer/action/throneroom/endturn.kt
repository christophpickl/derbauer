package com.github.christophpickl.derbauer.action.throneroom

import com.github.christophpickl.derbauer.model.Model

class ThroneRoomEndTurn {

    fun onEndTurn() {
        val throneRoom = Model.actions.throneRoom

        val newVisitors = throneRoom.computeNewVisitors()

        throneRoom.visitorsWaiting = Math.min(
            throneRoom.maxVisitorsWaiting,
            throneRoom.visitorsWaiting + newVisitors
        )
    }

}
