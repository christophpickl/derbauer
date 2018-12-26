package com.github.christophpickl.derbauer2.feature

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.Values
import com.github.christophpickl.derbauer2.military.Military
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Conditional
import com.github.christophpickl.derbauer2.model.Model

class MilitaryFeatures {

    val menu = AbstractFeature("New possibility available: Military") {
        Model.player.upgrades.militaryUpgrade.isMaxLevelReached
    }

    val knights = MilitaryCondition(Model.player.militaries.knights) {
        Model.player.buildings.barracks.amount >= Values.features.knightBarracksNeeded
    }

    val catapults = MilitaryCondition(Model.player.militaries.catapults) {
        Model.player.buildings.barracks.amount >= Values.features.catapultBarracksNeeded
    }

    @JsonIgnore val all = propertiesOfType<MilitaryFeatures, Feature>(this)
    
    override fun toString() = Stringifier.stringify(this)
}

class MilitaryCondition<M>(
    military: M,
    predicate: () -> Boolean
) : AbstractFeature(
    notification = "New military unit available: ${military.label}",
    predicate = predicate
) where M : Military, M : Conditional
