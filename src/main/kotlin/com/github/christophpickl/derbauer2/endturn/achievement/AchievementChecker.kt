package com.github.christophpickl.derbauer2.endturn.achievement

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.endturn.EndTurnScreen
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.AsciiArt
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.screen.InfoScreen
import com.github.christophpickl.derbauer2.ui.screen.Screen

object AchievementChecker {

    private val achievements = listOf(
        Trade1Achievement(),
        Attack1Achievement()
    )

    fun nextScreen(): Screen {
        val achieved = achievements.filter { it.conditionSatisfied() }
        if (achieved.isEmpty()) {
            return EndTurnScreen()
        }
        achieved.forEach {
            it.execute()
        }
        return AchievementScreen(
            "You are doing great: Achievement${if (achieved.size > 1) "s" else ""} unlocked.\n\n" +
                "${AsciiArt.achievement}\n\n" +
                achieved.joinToString("\n") { "- ${it.message}" }
        )
    }

}

class AchievementScreen(message: String) : InfoScreen(message) {
    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        Model.screen = EndTurnScreen()
    }
}
