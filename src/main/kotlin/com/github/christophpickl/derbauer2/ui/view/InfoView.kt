package com.github.christophpickl.derbauer2.ui.view

import com.github.christophpickl.derbauer2.ui.PromptMode

abstract class InfoView(message: String) : View {
    override val cancelSupport = CancelSupport.Disabled
    override val renderContent = message
    override val promptMode = PromptMode.Enter
}
