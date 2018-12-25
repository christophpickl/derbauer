package com.github.christophpickl.derbauer2.feature

import com.github.christophpickl.derbauer2.Values
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.model.Model

class BuildingFeature {

    private val castleCondition = FeatureCondition {
        Model.people >= Values.features.castlePeopleNeeded
    }
    val isCastleEnabled get() = castleCondition.checkAndGet()

    override fun toString() = Stringifier.stringify(this)
}
