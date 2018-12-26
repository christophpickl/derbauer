package com.github.christophpickl.derbauer.endturn.achievement

import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Entity
import com.github.christophpickl.derbauer.model.Model

class Trade1Achievement(
) : AbstractAchievement(label = "Trade Mastery I: Cheaper trade rates and +${Values.achievements.trade1GoldReward} gold") {
    override fun condition() = Model.history.traded >= Values.achievements.trade1HistoryNeed
    override fun execute() {
        Model.gold += Values.achievements.trade1GoldReward
        Model.player.resources.allTradeables.forEach {
            it.priceModifier -= Values.achievements.trade1PriceModifier
        }
    }
}

class Attack1Achievement(
) : AbstractAchievement(label = "Military Mastery I: Stronger military units") {
    override fun condition() = Model.history.attacked >= Values.achievements.attack1HistoryNeed
    override fun execute() {
        Model.player.militaries.all.forEach {
            it.attackModifier += Values.achievements.attack1AttackModifier
        }
    }
}

interface Achievement : Entity {
    fun conditionSatisfied(): Boolean
    fun execute()
}

abstract class AbstractAchievement(
    override val label: String
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