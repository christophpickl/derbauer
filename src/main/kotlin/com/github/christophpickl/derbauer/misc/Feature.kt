package com.github.christophpickl.derbauer.misc

import com.github.christophpickl.derbauer.model.DEPRECATED_CHEAT_MODE
import com.github.christophpickl.derbauer.model.State

@Deprecated(message = "v2")
class Feature {
    private var castleCondition = Condition({ State.player.people >= if (DEPRECATED_CHEAT_MODE) 10 else 100 })

    val isCastleEnabled get() = castleCondition.checkAndGet()

    fun reset() {
        castleCondition.reset()
    }
}

@Deprecated(message = "v2")
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
