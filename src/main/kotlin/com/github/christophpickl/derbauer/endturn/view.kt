package com.github.christophpickl.derbauer.endturn

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.data.AsciiArt
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.PromptMode
import com.github.christophpickl.derbauer.ui.view.InfoView

class EndTurnView(report: EndTurnReport) : InfoView(buildMessage(report)) {
    companion object {
        private fun buildMessage(report: EndTurnReport): String {
            val notificationsMessage = if (report.notifications.isEmpty()) "" else "\n\nGood news, everyone:\n\n" +
                report.notifications.joinToString("\n") { "- $it" }
            return "So, this is what happened over night:\n\n" +
                formatGrowth("Gold income    ", report.gold) + "\n" +
                formatGrowth("Food production", report.food) + "\n" +
                formatGrowth("People growth  ", report.people) +
                notificationsMessage
        }

        // TODO properly format: add left padding; add plus sign for change value
        private fun formatGrowth(label: String, line: EndTurnReportLine) =
            "$label: ${line.oldValue.formatted} => " +
                "${line.change.formatted} => " +
                line.newValue.formatted
    }

    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        callback.onEndTurn()
    }
}

class GameOverView : InfoView(buildMessage()) {
    companion object {
        fun buildMessage(): String {
            val suffix = when (Model.global.day) {
                in 0..5 -> "you looser!"
                in 6..10 -> "you beginner."
                in 11..20 -> "you lucky bastard."
                in 21..40 -> "of great leadership!"
                else -> "of bad ass ruler skills!!!!!!!!11111elf"
            }
            return "Game over!\n\n" +
                "${AsciiArt.gameOver}\n\n" +
                "R.I.P. after ${Model.global.day} days $suffix"
        }
    }

    override val promptMode = PromptMode.Off
    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        // no-op
    }
}
