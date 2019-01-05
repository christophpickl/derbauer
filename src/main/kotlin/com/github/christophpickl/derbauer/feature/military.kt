package com.github.christophpickl.derbauer.feature

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.military.Army
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import com.github.christophpickl.kpotpourri.common.string.Stringifier

class MilitaryFeatures {

    val menu = AbstractFeature("New menu available: Military") {
        Model.player.upgrades.militaryUpgrade.isMaxLevelReached
    }

    val knights = ArmyCondition(Model.player.armies.knights) {
        Model.player.buildings.barracks.amount >= Values.features.knightBarracksNeeded
    }

    val catapults = ArmyCondition(Model.player.armies.catapults) {
        Model.player.buildings.barracks.amount >= Values.features.catapultBarracksNeeded
    }

    @JsonIgnore val all = propertiesOfType<MilitaryFeatures, Feature>(this)
    
    override fun toString() = Stringifier.stringify(this)
}

class ArmyCondition<A>(
    army: A,
    predicate: () -> Boolean
) : AbstractFeature(
    notification = "New army available: ${army.label.capitalize()}",
    predicate = predicate
) where A : Army, A : Conditional
