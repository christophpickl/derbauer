package com.github.christophpickl.derbauer2.feature

import com.github.christophpickl.derbauer2.misc.Stringifier

class Feature {
    val building = BuildingFeature()
    val military = MilitaryFeature()
    val upgrade = UpgradeFeature()

    override fun toString() = Stringifier.stringify(this)
}

class FeatureCondition(val predicate: () -> Boolean) {

    private var wasEnabled = false

    fun checkAndGet(): Boolean =
        if (wasEnabled) {
            true
        } else {
            val checked = predicate()
            if (checked) {
                wasEnabled = true
            }
            checked
        }

}
