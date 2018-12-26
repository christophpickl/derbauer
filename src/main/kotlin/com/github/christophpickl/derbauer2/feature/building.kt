package com.github.christophpickl.derbauer2.feature

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.build.Building
import com.github.christophpickl.derbauer2.data.Values
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Conditional
import com.github.christophpickl.derbauer2.model.Model

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
