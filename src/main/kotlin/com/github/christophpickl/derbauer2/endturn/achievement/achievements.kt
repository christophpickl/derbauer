package com.github.christophpickl.derbauer2.endturn.achievement

import com.github.christophpickl.derbauer2.VALUES
import com.github.christophpickl.derbauer2.model.Model

class Trade1Achievement(
) : AbstractAchievement(message = "Trade Mastery I: Cheaper trade rates") {
    override fun condition() = Model.history.traded >= VALUES.achievementTrade1HistoryNeed
    override fun execute() {
        Model.player.resources.allTradeables.forEach {
            it.priceModifier -= VALUES.achievementTrade1PriceModifier
        }
    }
}

class Attack1Achievement(
) : AbstractAchievement(message = "Military Mastery I: Stronger military units") {
    override fun condition() = Model.history.attacked >= VALUES.achievementAttack1HistoryNeed
    override fun execute() {
        Model.player.militaries.all.forEach {
            it.attackModifier += 0.1
        }
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
