package com.github.christophpickl.derbauer.endturn

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.data.AsciiArt
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.view.InfoView

class Notifications {

    private val messages = mutableListOf<String>()

    fun add(message: String) {
        messages += message
    }

    fun consumeAll(): List<String> {
        val copy = ArrayList(messages)
        messages.clear()
        return copy
    }

}

class NotificationsView(notifications: List<String>) : InfoView(
    message = AsciiArt.feather + "\n\nGood news, everyone:\n\n" +
        notifications.joinToString("\n") { "- $it" }
) {
    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        callback.onEndTurn()
    }
}
