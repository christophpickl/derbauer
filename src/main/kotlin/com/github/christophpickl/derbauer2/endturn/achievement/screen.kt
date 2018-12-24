package com.github.christophpickl.derbauer2.endturn.achievement

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.endturn.EndTurnScreen
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.screen.InfoScreen

class AchievementScreen(message: String) : InfoScreen(message) {
    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        Model.screen = EndTurnScreen()
    }
}
