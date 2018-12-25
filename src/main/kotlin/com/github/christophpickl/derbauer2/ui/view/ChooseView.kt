package com.github.christophpickl.derbauer2.ui.view

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.model.Labeled
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.PromptMode
import com.github.christophpickl.derbauer2.ui.beep


abstract class ChooseView<C : Choice>(
    messages: List<String>,
    val choices: List<C>,
    val additionalContent: String? = null
) : View {

    override val renderContent =
        "${messages.random()}\n\n" +
            "Choose:\n\n${choices.mapIndexed { i, c -> "  [${i + 1}] ${c.label}" }.joinToString("\n")}" +
            if (additionalContent != null) "\n\n$additionalContent" else ""
    
    override val promptMode = PromptMode.Input

    abstract fun onCallback(callback: ViewCallback, choice: C)

    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        when (input) {
            PromptInput.Empty -> {
                handleCancel()
            }
            is PromptInput.Number -> {
                if (input.number < 1 || input.number > choices.size) {
                    beep("Invalid input choice: ${input.number} (must be within 1 and ${choices.size})")
                } else {
                    onCallback(callback, choices[input.number - 1])
                }
            }
        }.enforceWhenBranches()
    }
}

interface Choice : Labeled

class EnumChoice<E : Enum<E>>(
    val enum: E,
    override val label: String
) : Choice