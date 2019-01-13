package com.github.christophpickl.derbauer.ui.view

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.buysell.Buyable
import com.github.christophpickl.derbauer.misc.enforceWhenBranches
import com.github.christophpickl.derbauer.model.Describable
import com.github.christophpickl.derbauer.model.Labeled
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.derbauer.ui.Beeper
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.PromptMode
import com.github.christophpickl.derbauer.ui.RealBeeper


abstract class ChooseView<C : Choice>(
    message: String,
    private val choices: List<C>,
    val additionalContent: String? = null,
    private val beeper: Beeper = RealBeeper,
    cancelSupport: CancelSupport,
    private val cancelHandler: CancelHandler = CancelHandlerDelegate(cancelSupport, beeper)
) : View, CancelHandler by cancelHandler {

    init {
        require(choices.filter { it.isZeroChoice() }.count() <= 1) {
            "only max one zero choice possible! choices: ${choices.joinToString()}"
        }
        if (choices.any { it.isZeroChoice() }) {
            require(choices.last().isZeroChoice()) { "if using zero choice, then it must be last index!" }
        }
    }

    private val zeroChoice: C? = choices.firstOrNull { it.isZeroChoice() }

    private var choiceCounter = 1

    override val renderContent =
        "$message\n\n" +
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
                val context = InputContext.by(input, choices, zeroChoice)
                if (context.isWithinRange()) {
                    onCallback(callback, context.lookupChoice())
                } else {
                    beeper.beep(context.beepMessage())
                }
            }
        }.enforceWhenBranches()
    }

}

interface Choice : Labeled {
    fun isZeroChoice(): Boolean = false

    fun formatLabel(pre: String, post: String) = "$pre ... $post"

    fun <B> formatLabel(
        buyable: B,
        currentAmount: Amount? = null
    ) where B : Labeled, B : Buyable, B : Describable =
        formatLabel(
            pre = buyable.label.capitalize() + if (currentAmount != null) " (${currentAmount.formatted})" else "",
            post = "${buyable.buyDescription} (${buyable.description})"
        )
}

class EnumChoice<E : Enum<E>>(
    val enum: E,
    override val label: String,
    val zeroChoice: Boolean = false
) : Choice {
    override fun isZeroChoice(): Boolean = zeroChoice
    override fun toString() = "EnumChoice{enum=$enum, label=$label, zeroChoice=$zeroChoice}"
}

private data class InputContext<C : Choice>(
    private val input: PromptInput.Number,
    private val startIndex: Int,
    private val endIndex: Int,
    private val additionalBeep: String,
    val lookupChoice: () -> C
) {
    companion object {
        fun <C : Choice> by(input: PromptInput.Number, choices: List<C>, zeroChoice: C?): InputContext<C> {
            val startIndex: Int
            val endIndex: Int
            val additionalBeep: String
            val defaultLookupChoice: () -> C = {
                choices[input.number.toInt() - 1]
            }
            val lookupChoice: () -> C
            if (zeroChoice != null) {
                startIndex = 0
                endIndex = choices.size - 1
                additionalBeep = ", or 0 for zeroChoice"
                lookupChoice = {
                    if (input.number.toInt() == 0) {
                        zeroChoice
                    } else {
                        defaultLookupChoice()
                    }
                }
            } else {
                startIndex = 1
                endIndex = choices.size
                additionalBeep = ""
                lookupChoice = defaultLookupChoice
            }
            return InputContext(
                input = input,
                startIndex = startIndex,
                endIndex = endIndex,
                additionalBeep = additionalBeep,
                lookupChoice = lookupChoice
            )
        }
    }

    fun isWithinRange() = (input.number >= startIndex) && (input.number <= endIndex)
    fun beepMessage() = "Invalid input choice: ${input.number} (must be within 1 and $endIndex$additionalBeep)"

}
