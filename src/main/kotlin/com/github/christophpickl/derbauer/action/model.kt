package com.github.christophpickl.derbauer.action

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Describable
import com.github.christophpickl.derbauer.model.Entity
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.Ordered
import com.github.christophpickl.derbauer.model.filterConditional
import com.github.christophpickl.derbauer.model.ordered
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType

@Suppress("unused")
class Actions {
    val throneRoom: ThroneRoomAction = ThroneRoomAction()

    @get:JsonIgnore val all get() = propertiesOfType<Actions, Action>(this).ordered().filterConditional()

}

interface Action : Entity, Conditional, Describable, Ordered {
    fun onSpecificAction(on: OnSpecificAction)
}

interface OnSpecificAction {
    fun onSpecificThroneRoomAction()
}

class ThroneRoomAction(
    override val label: String = "visit throne room"
) : Action {

    var visitorsWaiting = Values.actions.throneRoom.initialVisitorsWaiting
    val maxVisitorsWaiting get() = computeNewVisitors() * 3

    fun computeNewVisitors() = if (Model.player.buildings.castles.amount.isNotZero) {
        1 + Math.log10(Model.player.buildings.castles.amount.real.toDouble()).toInt()
    } else 0
    
    @get:JsonIgnore override val order: Int = 0
    override val description
        get() = "$visitorsWaiting " +
            "visitor${if (visitorsWaiting == 1) "" else "s"} waiting"

    override fun checkCondition() = Model.features.action.throneRoomEnabled.isEnabled()
    override fun onSpecificAction(on: OnSpecificAction) {
        on.onSpecificThroneRoomAction()
    }

}
