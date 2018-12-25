package com.github.christophpickl.derbauer2.feature

import com.github.christophpickl.derbauer2.VALUES
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.model.Model

class Feature {

    private val castleCondition = Condition { Model.people >= VALUES.featureCastlePeopleNeeded }
    val isCastleEnabled get() = castleCondition.checkAndGet()

    override fun toString() = Stringifier.stringify(this)

}

private class Condition(val predicate: () -> Boolean) {

    private var wasEnabled = false

    fun checkAndGet(): Boolean =
        if (wasEnabled) true else {
            predicate().also {
                if (it) wasEnabled = true
            }
        }

    fun reset() {
        wasEnabled = false
    }
}
