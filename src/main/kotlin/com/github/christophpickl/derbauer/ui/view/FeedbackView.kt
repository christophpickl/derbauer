package com.github.christophpickl.derbauer.ui.view

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.PromptInput

class FeedbackView(
    appendMessage: String,
    private val doAfterwards: () -> Unit
) : InfoView(
    message = concatMessages(Model.currentView.renderContent, appendMessage)
) {

    companion object {
        fun concatMessages(oldMessage: String, appendMessage: String) =
            "$oldMessage\n\n-------------------\n\n$appendMessage"
    }

    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        doAfterwards()
    }
}
