package com.github.christophpickl.derbauer2.action

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.Values
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.ConditionalEntity
import com.github.christophpickl.derbauer2.model.Descriptable
import com.github.christophpickl.derbauer2.model.Entity
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.Ordered
import com.github.christophpickl.derbauer2.model.filterConditional
import com.github.christophpickl.derbauer2.model.ordered

interface Action : Entity, Descriptable, Ordered {
    fun onSpecificAction(on: OnSpecificAction)
}

interface OnSpecificAction {
    fun onSpecificThroneRoomAction()
}

class Actions {
    val visitThroneRoom: ThroneRoomAction = ThroneRoomAction()

    @get:JsonIgnore val all get() = propertiesOfType<Actions, Action>(this).ordered().filterConditional()

    var visitorsWaitingInThroneRoom: Int = Values.actions.visitorsWaitingInThroneRoom
}

class ThroneRoomAction(
    override val label: String = "visit throne room"
) : Action, ConditionalEntity {

    override val order: Int = 0
    override val description get() = "${Model.actions.visitorsWaitingInThroneRoom} visitor${if (Model.actions.visitorsWaitingInThroneRoom == 1) "" else "s"} waiting"
    override fun checkCondition() = Model.player.buildings.castles.amount > 0
    override fun onSpecificAction(on: OnSpecificAction) {
        on.onSpecificThroneRoomAction()
    }

}
