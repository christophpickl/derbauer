package com.github.christophpickl.derbauer.feature

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.build.Building
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import com.github.christophpickl.kpotpourri.common.string.Stringifier

class BuildingFeatures {

    val castleEnabled = BuildingFeature(Model.player.buildings.castles) {
        Model.people >= Values.features.castlePeopleNeeded
    }

    @JsonIgnore val all = propertiesOfType<BuildingFeatures, Feature>(this)

    override fun toString() = Stringifier.stringify(this)
}

class BuildingFeature<B>(
    building: B,
    predicate: () -> Boolean
) : AbstractFeature(
    "New building available: ${building.label}", predicate
) where B : Building, B : Conditional