package com.github.christophpickl.derbauer2.ui.screen

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.PromptMode

interface Screen {
    val renderContent: String
    val promptMode: PromptMode
    val cancelSupport: CancelSupport

    fun onCallback(callback: ScreenCallback, input: PromptInput)
}
