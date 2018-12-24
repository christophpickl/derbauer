package com.github.christophpickl.derbauer2.domain

import com.github.christophpickl.derbauer2.InfoScreen
import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.state.Model
import com.github.christophpickl.derbauer2.ui.PromptInput

class EndTurnScreen : InfoScreen("This is the end...") {
    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        Model.goHome()
    }
}
