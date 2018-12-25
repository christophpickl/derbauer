package com.github.christophpickl.derbauer2.endturn.achievement

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.ui.AsciiArt
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.view.InfoView
import com.github.christophpickl.derbauer2.ui.view.View

object AchievementChecker {

    private val achievements = listOf(
        Trade1Achievement(),
        Attack1Achievement()
    )

    fun nextView(): View? {
        val achieved = achievements.filter { it.conditionSatisfied() }
        if (achieved.isEmpty()) {
            return null
        }
        achieved.forEach {
            it.execute()
        }
        return AchievementView(
            "You are doing great: Achievement${if (achieved.size > 1) "s" else ""} unlocked.\n\n" +
                "${AsciiArt.achievement}\n\n" +
                achieved.joinToString("\n") { "- ${it.message}" }
        )
    }

}

class AchievementView(message: String) : InfoView(message) {
    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        callback.goEndTurnReport()
    }
}
