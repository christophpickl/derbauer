package com.github.christophpickl.derbauer.trade

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.misc.AbstractFeature
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import com.github.christophpickl.kpotpourri.common.string.Stringifier

class TradeFeatures {

    val landEnabled = TradeFeature(
        notification = "New possibility to also trade land."
    ) {
        val landLeft = Model.land - Model.player.buildings.totalLandNeeded
        landLeft.isZero
    }

    @JsonIgnore val all = propertiesOfType<TradeFeatures, TradeFeature>(this)

    override fun toString() = Stringifier.stringify(this)
}

class TradeFeature(
    notification: String,
    predicate: () -> Boolean
) : AbstractFeature(notification, predicate)
