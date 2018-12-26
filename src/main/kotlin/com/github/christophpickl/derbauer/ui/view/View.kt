package com.github.christophpickl.derbauer.ui.view

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.PromptMode

interface View {
    @get:JsonIgnore val renderContent: String
    val promptMode: PromptMode
    val cancelSupport: CancelSupport

    /** Invoked when the router gets an onEnter from the keyboard prompt. */
    fun onCallback(callback: ViewCallback, input: PromptInput)
}
