package com.github.christophpickl.derbauer.feature

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.military.Military
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import com.github.christophpickl.kpotpourri.common.string.Stringifier

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
    notification = "New military unit available: ${military.label.capitalize()}",
    predicate = predicate
) where M : Military, M : Conditional
