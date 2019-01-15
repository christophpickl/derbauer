package com.github.christophpickl.derbauer.endturn.achievement

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.endturn.EndTurnAchievements
import com.github.christophpickl.derbauer.military.MilitaryAchievements
import com.github.christophpickl.derbauer.model.Entity
import com.github.christophpickl.derbauer.trade.TradeAchievements
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType

@Suppress("unused")
class Achievements {
    val endTurn = EndTurnAchievements()
    val trade = TradeAchievements()
    val military = MilitaryAchievements()

    @get:JsonIgnore val all
        get() = propertiesOfType<Achievements, Achievement>(this)
            .plus(endTurn.all)
            .plus(trade.all)
            .plus(military.all)

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

    /**
     * @return true if state was changed (not to confuse with the actual isAchieved result by condition).
     */
    final override fun conditionSatisfied(): Boolean {
        if (!isAchieved && condition()) {
            isAchieved = true
            return true
        }
        return false
    }
}
