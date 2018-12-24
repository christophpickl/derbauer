package com.github.christophpickl.derbauer2.misc

import com.github.christophpickl.derbauer2.State

class Renderer(
    private val text: MainTextArea,
    private val prompt: Prompt
) {
    fun render() {
        val promptText = when (State.screen.promptMode) {
            PromptMode.Off -> ""
            PromptMode.Enter -> "\n\nHit ENTER >>"
            PromptMode.Input -> "\n\n$ ${prompt.enteredText}"
        }
        val content = State.screen.renderContent
        text.text = "$content$promptText"
    }
}
