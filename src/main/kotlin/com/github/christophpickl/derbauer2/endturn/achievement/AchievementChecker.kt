package com.github.christophpickl.derbauer2.endturn.achievement

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.AsciiArt
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.view.InfoView
import com.github.christophpickl.derbauer2.ui.view.View

class Achievements {
    val trade1 = Trade1Achievement()
    val attack1 = Attack1Achievement()

    @get:JsonIgnore val all get() = propertiesOfType<Achievements, Achievement>(this)
}


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

class AchievementView(message: String) : InfoView(message) {
    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        callback.goEndTurnReport()
    }
}
