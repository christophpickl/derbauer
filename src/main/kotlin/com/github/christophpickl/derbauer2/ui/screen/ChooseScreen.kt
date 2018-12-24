package com.github.christophpickl.derbauer2.ui.screen

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.model.Labeled
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.PromptMode
import com.github.christophpickl.derbauer2.ui.beep


abstract class ChooseScreen<C : Choice>(
    messages: List<String>,
    val choices: List<C>,
    val additionalContent: String? = null
) : Screen {

    override val renderContent =
        "${messages.random()}\n\n" +
            "Choose:\n${choices.mapIndexed { i, c -> "  [${i + 1}] ${c.label}" }.joinToString("\n")}" +
            if (additionalContent != null) "\n\n$additionalContent" else ""
    
    override val promptMode = PromptMode.Input

    abstract fun onCallback(callback: ScreenCallback, choice: C)

    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        when (input) {
            PromptInput.Empty -> {
                handleCancel()
            }
            is PromptInput.Number -> {
                if (input.number < 1 || input.number > choices.size) {
                    beep()
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
