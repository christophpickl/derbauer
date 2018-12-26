package com.github.christophpickl.derbauer.action.throneRoom

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.VIEW_SIZE
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.Choice
import com.github.christophpickl.derbauer.ui.view.ChooseView
import com.github.christophpickl.derbauer.ui.view.InfoView
import com.github.christophpickl.derbauer.ui.view.YesNoCallback
import com.github.christophpickl.derbauer.ui.view.YesNoChooseView
import com.github.christophpickl.kpotpourri.common.string.times

class ThroneRoomView(
    private val visitor: ThroneRoomVisitor
) : ChooseView<ThroneRoomChoice>(
    messages = listOf(visitor.message),
    choices = visitor.choices
) {
    override val cancelSupport = CancelSupport.Disabled
    override fun onCallback(callback: ViewCallback, choice: ThroneRoomChoice) {
        callback.onThroneRoomChoice(visitor, choice)
    }
}

class ThroneRoomChoice(
    val id: Int,
    override val label: String
) : Choice

class ThroneRoomEmptyView(responseFromPreviousVisitor: String? = null) : InfoView(
    (if (responseFromPreviousVisitor != null) "$responseFromPreviousVisitor\n\n" + "${"-".times(VIEW_SIZE.first)}\n\n" else "") +
        "There are no visitors waiting for you, my lord.\n\n" +
        "Please come back some other day again."
) {
    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        Model.goHome()
    }
}

class ThroneRoomChoosenView(lastVisitorResponse: String, callback: YesNoCallback) : YesNoChooseView(
    yesNoCallback = callback,
    message = buildMessage(lastVisitorResponse)
) {
    companion object {
        fun buildMessage(lastVisitorResponse: String): String {
            val visitors = Model.actions.visitorsWaitingInThroneRoom
            val singular = visitors == 1
            return "$lastVisitorResponse\n\n${"-".times(VIEW_SIZE.first)}\n\n" +
                "There ${if (singular) "is" else "are"} $visitors visitor${if (singular) "" else "s"} waiting for you.\n" +
                "Do you wish to welcome the next visitor?"
        }
    }
}
