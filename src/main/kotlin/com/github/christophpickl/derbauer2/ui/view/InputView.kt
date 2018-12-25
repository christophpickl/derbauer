package com.github.christophpickl.derbauer2.ui.view

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.PromptMode

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
