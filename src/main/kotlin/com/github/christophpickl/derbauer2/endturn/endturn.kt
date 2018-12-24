package com.github.christophpickl.derbauer2.endturn

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.screen.InfoScreen

class EndTurnScreen : InfoScreen(
    "This is the end..."
) {
    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        // TODO calc happening
        Model.goHome()
    }
}
