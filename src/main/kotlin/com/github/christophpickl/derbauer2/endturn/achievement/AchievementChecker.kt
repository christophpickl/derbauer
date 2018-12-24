package com.github.christophpickl.derbauer2.endturn.achievement

import com.github.christophpickl.derbauer2.CHEAT_MODE
import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.endturn.EndTurnScreen
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.screen.InfoScreen
import com.github.christophpickl.derbauer2.ui.screen.Screen

class AchievementScreen(message: String) : InfoScreen(message) {
    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        Model.screen = EndTurnScreen()
    }
}

object AchievementChecker {

    private val achievements = listOf(
        Trade1Achievement()
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
                achieved.joinToString("\n") { "- ${it.message}" }
        )
    }

}

class Trade1Achievement : Achievement {
    override fun conditionSatisfied() = Model.history.traded > if (CHEAT_MODE) 1 else 10
    override val message = "Trade Mastery I: Cheaper trade rates"
    override fun execute() {
        Model.player.resources.allTradeables.forEach {
            it.priceModifier -= if (CHEAT_MODE) 0.4 else 0.1
        }
    }
}

// TODO army attack

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

    override fun conditionSatisfied(): Boolean {
        if (!isAchieved && condition()) {
            isAchieved = true
            return true
        }
        return false
    }
}

