package com.github.christophpickl.derbauer.endturn.achievement

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.view.InfoView

class AchievementView(message: String) : InfoView(message) {
    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        callback.goEndTurnReport()
    }
}
