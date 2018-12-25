package com.github.christophpickl.derbauer2

import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.model.Model

class Feature {
    val building = BuildingFeature()
    val military = MilitaryFeature()

    override fun toString() = Stringifier.stringify(this)
}

class BuildingFeature {
    private val castleCondition = Condition { Model.people >= Values.features.castlePeopleNeeded }
    val isCastleEnabled get() = castleCondition.checkAndGet()

    override fun toString() = Stringifier.stringify(this)
}

class MilitaryFeature {

    var isMilitaryEnabled = if (CHEAT_MODE) true else false

    private val knightCondition = Condition { Model.player.buildings.barracks.amount >= Values.features.knightBarracksNeeded }
    val isKnightEnabled get() = knightCondition.checkAndGet()
    private val catapultCondition = Condition { Model.player.buildings.barracks.amount >= Values.features.catapultBarracksNeeded }
    val isCatapultEnabled get() = catapultCondition.checkAndGet()

    override fun toString() = Stringifier.stringify(this)
}

private class Condition(val predicate: () -> Boolean) {

    private var wasEnabled = false

    fun checkAndGet(): Boolean =
        if (wasEnabled) true else {
            predicate().also {
                if (it) wasEnabled = true
            }
        }

}
