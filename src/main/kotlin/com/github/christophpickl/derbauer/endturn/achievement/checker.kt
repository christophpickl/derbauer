package com.github.christophpickl.derbauer.endturn.achievement

import com.github.christophpickl.derbauer.data.AsciiArt
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.view.View

object AchievementChecker {
    fun nextView(): View? {
        val achieved = Model.achievements.all.filter { it.conditionSatisfied() }
        if (achieved.isEmpty()) {
            return null
        }
        achieved.forEach {
            it.execute()
        }
        return AchievementView(
            "You are doing great: Achievement${if (achieved.size > 1) "s" else ""} unlocked\n\n" +
                "${AsciiArt.achievement}\n\n" +
                achieved.joinToString("\n") { it.label }
        )
    }
}
