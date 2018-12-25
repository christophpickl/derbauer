package com.github.christophpickl.derbauer2.feature

import com.github.christophpickl.derbauer2.Values
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.model.Model

class MilitaryFeature {

    private val militaryCondition = FeatureCondition { Model.player.upgrades.militaryUpgrade.isMaxLevelReached }
    val isMilitaryEnabled get() = militaryCondition.checkAndGet()

    private val knightCondition = FeatureCondition { Model.player.buildings.barracks.amount >= Values.features.knightBarracksNeeded }
    val isKnightEnabled get() = knightCondition.checkAndGet()

    private val catapultCondition = FeatureCondition { Model.player.buildings.barracks.amount >= Values.features.catapultBarracksNeeded }
    val isCatapultEnabled get() = catapultCondition.checkAndGet()

    override fun toString() = Stringifier.stringify(this)
}

