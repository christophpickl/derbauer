package com.github.christophpickl.derbauer.action

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.misc.AbstractFeature
import com.github.christophpickl.derbauer.misc.Feature
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import com.github.christophpickl.kpotpourri.common.string.Stringifier

class ActionFeatures {

    val throneRoomEnabled = ActionFeature("Throne room") {
        Model.player.buildings.castles.amount > 0
    }

    @JsonIgnore val all = propertiesOfType<ActionFeatures, Feature>(this)

    override fun toString() = Stringifier.stringify(this)
}

class ActionFeature(
    label: String,
    predicate: () -> Boolean
) : AbstractFeature(
    "New action available: $label", predicate
)
