package com.github.christophpickl.derbauer.achievement

import com.github.christophpickl.derbauer.logic.Screen
import com.github.christophpickl.derbauer.logic.ScreenCallback
import com.github.christophpickl.derbauer.logic.SimpleMessageScreen

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
