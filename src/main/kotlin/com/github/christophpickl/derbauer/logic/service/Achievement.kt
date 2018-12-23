package com.github.christophpickl.derbauer.logic.service

import com.github.christophpickl.derbauer.logic.screens.Screen
import com.github.christophpickl.derbauer.logic.screens.ScreenCallback
import com.github.christophpickl.derbauer.logic.screens.SimpleMessageScreen
import com.github.christophpickl.derbauer.model.CHEAT_MODE
import com.github.christophpickl.derbauer.model.State

class AchievementScreen(message: String) : SimpleMessageScreen(message) {
    override fun onCallback(callback: ScreenCallback) {
        callback.onAchievement(this)
    }
}

class AchievementHappener {

    private val allAchievements: List<Achievement> = listOf(
        Trade1Achievement(),
        Army1Achievement()
    )

    fun anyHappened(): Screen? {
        val achievements = allAchievements.mapNotNull { it.check() }
        return if (achievements.isEmpty()) null else {
            AchievementScreen(message = "Congratulations!\nNew achievement${if (achievements.size == 1) "" else "s"} unlocked:\n\n" +
                achievements.joinToString("\n"))
        }
    }

}

class Trade1Achievement() : Achievement(
    message = "Trade Mastery I: Cheaper trade rates"
) {
    private val tradeThreshold = if (CHEAT_MODE) 1 else 10

    override fun condition() =
        State.history.traded >= tradeThreshold

    override fun changeState() {
        State.prices.trade.decreaseAllBy(0.2)
    }

}

class Army1Achievement() : Achievement(
    message = "Military Mastery I: Soldier attack +30%"
) {
    private val attackThreshold = if (CHEAT_MODE) 1 else 10

    override fun condition() =
        State.history.attacked >= attackThreshold

    override fun changeState() {
        State.army.soldierAttackStrength += 0.3
    }

}

abstract class Achievement(
    val message: String
) {
    private var isAchieved = false
    protected abstract fun condition(): Boolean
    protected abstract fun changeState()

    fun check(): String? {
        if (!isAchieved && condition()) {
            isAchieved = true
            changeState()
            return message
        }
        return null
    }
}
