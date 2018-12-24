package com.github.christophpickl.derbauer2.misc

import com.github.christophpickl.derbauer.logic.beep
import com.github.christophpickl.derbauer2.state.State

interface Screen {
    val renderContent: String
    val promptMode: PromptMode
    val cancelSupport: CancelSupport

    fun onCallback(callback: ScreenCallback, input: PromptInput)
}

sealed class CancelSupport {
    object Disabled : CancelSupport()
    class Enabled(val targetScreen: () -> Screen) : CancelSupport()
}

private fun handleCancel() {
    when (val cancel = State.screen.cancelSupport) {
        CancelSupport.Disabled -> beep()
        is CancelSupport.Enabled -> {
            State.screen = cancel.targetScreen()
        }
    }
}

abstract class InfoScreen(message: String) : Screen {
    override val cancelSupport = CancelSupport.Disabled
    override val renderContent = message
    override val promptMode = PromptMode.Enter
}

abstract class ChooseScreen<C : Choice>(
    message: String,
    val choices: List<C>
) : Screen {

    override val renderContent = "$message\n\nChoose:\n${choices.mapIndexed { i, c -> "  [${i + 1}] ${c.label}" }.joinToString("\n")}"
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

class EnumChoice<E : Enum<E>>(
    val enum: E,
    override val label: String
) : Choice

interface Choice {
    val label: String
}
