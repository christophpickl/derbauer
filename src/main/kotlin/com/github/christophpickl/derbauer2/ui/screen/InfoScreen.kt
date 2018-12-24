package com.github.christophpickl.derbauer2.ui.screen

import com.github.christophpickl.derbauer2.ui.PromptMode

abstract class InfoScreen(message: String) : Screen {
    override val cancelSupport = CancelSupport.Disabled
    override val renderContent = message
    override val promptMode = PromptMode.Enter
}
