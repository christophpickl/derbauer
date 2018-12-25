package com.github.christophpickl.derbauer2.ui.view

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.PromptMode

interface View {
    @get:JsonIgnore val renderContent: String
    val promptMode: PromptMode
    val cancelSupport: CancelSupport

    fun onCallback(callback: ViewCallback, input: PromptInput)
}
