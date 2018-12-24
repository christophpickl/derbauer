package com.github.christophpickl.derbauer2.endturn.achievement

import com.github.christophpickl.derbauer2.CHEAT_MODE
import com.github.christophpickl.derbauer2.model.Model

class Trade1Achievement(
) : AbstractAchievement(message = "Trade Mastery I: Cheaper trade rates") {
    override fun condition() = Model.history.traded > if (CHEAT_MODE) 1 else 10
    override fun execute() {
        Model.player.resources.allTradeables.forEach {
            it.priceModifier -= if (CHEAT_MODE) 0.4 else 0.1
        }
    }
}

class Attack1Achievement(
) : AbstractAchievement(message = "Military Mastery I: Soldier attack +30%") {
    override fun condition() = Model.history.attacked > if (CHEAT_MODE) 1 else 5
    override fun execute() {
        // FIXME increase achievement
    }
}

interface Achievement {
    val message: String
    fun conditionSatisfied(): Boolean
    fun execute()
}

abstract class AbstractAchievement(
    override val message: String
) : Achievement {
    private var isAchieved = false
    protected abstract fun condition(): Boolean

    final override fun conditionSatisfied(): Boolean {
        if (!isAchieved && condition()) {
            isAchieved = true
            return true
        }
        return false
    }
}
