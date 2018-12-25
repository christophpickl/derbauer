package com.github.christophpickl.derbauer2.ui.view

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.model.Labeled
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.PromptMode
import com.github.christophpickl.derbauer2.ui.beep


abstract class ChooseView<C : Choice>(
    messages: List<String>,
    private val choices: List<C>,
    val additionalContent: String? = null
) : View {

    init {
        require(choices.filter { it.isZeroChoice() }.count() <= 1) { "only max one zero choice possible! choices: ${choices.joinToString()}" }
        if (choices.any { it.isZeroChoice() }) {
            require(choices.last().isZeroChoice()) { "if using zero choice, then it must be last index!" }
        }
    }

    private var zeroChoice: C? = choices.firstOrNull { it.isZeroChoice() }

    private var choiceCounter = 1
    override val renderContent =
        "${messages.random()}\n\n" +
            "Choose:\n\n" +
            choices.joinToString("\n") { c -> "  [${if (c.isZeroChoice()) 0 else choiceCounter++}] ${c.label}" } +
            if (additionalContent != null) "\n\n$additionalContent" else ""

    override val promptMode = PromptMode.Input

    abstract fun onCallback(callback: ViewCallback, choice: C)

    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        when (input) {
            PromptInput.Empty -> {
                handleCancel()
            }
            is PromptInput.Number -> {
                if ((input.number >= if (zeroChoice != null) 0 else 1) &&
                    (input.number <= if (zeroChoice != null) choices.size - 1 else choices.size)) {
                    onCallback(callback, if (input.number == 0) zeroChoice!! else choices[input.number - 1])
                } else {
                    beep("Invalid input choice: ${input.number} (must be within 1 and ${choices.size})")
                }
            }
        }.enforceWhenBranches()
    }
}

interface Choice : Labeled {
    fun isZeroChoice(): Boolean = false
}

class EnumChoice<E : Enum<E>>(
    val enum: E,
    override val label: String,
    val zeroChoice: Boolean = false
) : Choice {
    override fun isZeroChoice(): Boolean = zeroChoice
    override fun toString() = "EnumChoice{enum=$enum, label=$label, zeroChoice=$zeroChoice}"
}
