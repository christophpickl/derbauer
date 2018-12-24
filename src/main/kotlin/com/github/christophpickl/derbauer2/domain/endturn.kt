package com.github.christophpickl.derbauer2.domain

import com.github.christophpickl.derbauer2.State
import com.github.christophpickl.derbauer2.misc.InfoScreen
import com.github.christophpickl.derbauer2.misc.PromptInput
import com.github.christophpickl.derbauer2.misc.ScreenCallback

class EndTurnScreen : InfoScreen("This is the end...") {
    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        State.goHome()
    }
}