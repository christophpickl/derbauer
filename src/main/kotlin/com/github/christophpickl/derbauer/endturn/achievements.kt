package com.github.christophpickl.derbauer.endturn

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.endturn.achievement.Achievement
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType

@Suppress("unused")
class EndTurnAchievements {
    val daily = DailyAchievement()

    @get:JsonIgnore val all get() = propertiesOfType<EndTurnAchievements, Achievement>(this)
}

class DailyAchievement : Achievement {

    private var currentLevel = 1
    private val upcomingDay get() = Model.global.day + 1
    private val eachXDays = 50
    private val minimumReward = Amount(100L)
    private val rewardSizeRelativeToWealth = 0.1

    override val label get() = "You reached day $upcomingDay and got ${calculateRewardGold().formatted} gold."

    override fun conditionSatisfied() =
        if (upcomingDay == currentLevel * eachXDays) {
            currentLevel++
            true
        } else false

    override fun execute() {
        Model.gold += calculateRewardGold()
    }

    private fun calculateRewardGold() =
        Amount.maxOf(minimumReward, Model.player.relativeWealthBy(rewardSizeRelativeToWealth))

}
