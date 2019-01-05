package com.github.christophpickl.derbauer.action.throneroom

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.Choice
import com.github.christophpickl.derbauer.ui.view.ChooseView
import com.github.christophpickl.derbauer.ui.view.InfoView
import com.github.christophpickl.derbauer.ui.view.YesNoCallback
import com.github.christophpickl.derbauer.ui.view.YesNoChooseView

class ThroneRoomView<C : ThroneRoomChoice>(
    private val visitor: ThroneRoomVisitor<C>
) : ChooseView<C>(
    choosePrompt = visitor.choosePrompt,
    messages = listOf(visitor.message),
    choices = visitor.choices,
    cancelSupport = CancelSupport.Disabled
) {
    override fun onCallback(callback: ViewCallback, choice: C) {
        callback.onThroneRoomChoice(visitor, choice)
    }
}

interface ThroneRoomChoice : Choice

class ThroneRoomEmptyView : InfoView(
    "The throne room is empty and no visitors are currently waiting for you, my lord.\n" +
        "\n" +
        "Please come back some other day again."
) {
    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        Model.goHome()
    }
}

class ThroneRoomChoosenView(callback: YesNoCallback) : YesNoChooseView(
    yesNoCallback = callback,
    message = buildMessage()
) {
    companion object {
        fun buildMessage(): String {
            val visitors = Model.actions.visitorsWaitingInThroneRoom
            val singular = visitors == 1
            val areVisitors = "${if (singular) "is" else "are"} $visitors visitor${if (singular) "" else "s"}"
            return "There $areVisitors waiting for you.\n" +
                "Do you wish to welcome the next visitor?"
        }
    }
}
