package com.github.christophpickl.derbauer2.ui.screen

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.PromptMode

abstract class InputScreen(message: String) : Screen {
    override val renderContent = message
    override val promptMode = PromptMode.Input

    abstract fun onCallback(callback: ScreenCallback, number: Int)

    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        when (input) {
            PromptInput.Empty -> {
                handleCancel()
            }
            is PromptInput.Number -> {
                onCallback(callback, input.number)
            }
        }.enforceWhenBranches()
    }

}
