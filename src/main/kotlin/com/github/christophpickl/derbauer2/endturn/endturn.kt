package com.github.christophpickl.derbauer2.endturn

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.endturn.happening.Happener
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.screen.InfoScreen

class EndTurnScreen : InfoScreen(
    "This is the end..."
) {
    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        val happening = Happener.letItHappen()
        if (happening != null) {
            Model.screen = happening
        } else {
            Model.goHome()
        }
    }
}
