package com.github.christophpickl.derbauer2.feature

import com.github.christophpickl.derbauer2.CHEAT_MODE
import com.github.christophpickl.derbauer2.model.Model

class Feature {
    private val castleCondition = Condition { Model.people >= if (CHEAT_MODE) 10 else 100 }

    val isCastleEnabled get() = castleCondition.checkAndGet()
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
