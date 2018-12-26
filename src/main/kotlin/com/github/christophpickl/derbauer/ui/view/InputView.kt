package com.github.christophpickl.derbauer.ui.view

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.misc.enforceWhenBranches
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.PromptMode

abstract class InputView(message: String) : View {
    override val renderContent = message
    override val promptMode = PromptMode.Input

    abstract fun onCallback(callback: ViewCallback, number: Int)

    override fun onCallback(callback: ViewCallback, input: PromptInput) {
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
