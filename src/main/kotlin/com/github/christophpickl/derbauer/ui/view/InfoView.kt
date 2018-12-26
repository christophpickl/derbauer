package com.github.christophpickl.derbauer.ui.view

import com.github.christophpickl.derbauer.ui.PromptMode

abstract class InfoView(message: String) : View {
    override val cancelSupport = CancelSupport.Disabled
    override val renderContent = message
    override val promptMode = PromptMode.Enter
}
