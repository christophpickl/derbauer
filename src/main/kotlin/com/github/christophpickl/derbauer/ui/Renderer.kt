package com.github.christophpickl.derbauer.ui

import com.github.christophpickl.derbauer.model.LimitedAmount
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.UsableEntity
import com.github.christophpickl.derbauer.resource.AbstracteResource
import com.github.christophpickl.kpotpourri.common.string.splitMaxWidth
import com.github.christophpickl.kpotpourri.common.string.times
import mu.KotlinLogging.logger

interface Renderer {
    fun render()
}

class RendererImpl(
    private val text: MainTextArea,
    private val prompt: Prompt
) : Renderer {

    override fun render() {
        val promptText = when (Model.currentView.promptMode) {
            PromptMode.Off -> null
            PromptMode.Enter -> "Hit ENTER to continue . . ."
            PromptMode.Input -> "$ ${prompt.enteredText}⌷"
        }
        val content = Model.currentView.renderContent

        val info = Model.player.resources.all.joinToString("  ") { it.formatInfo() }

        val board = Board()
        board.printHeader("Day: ${Model.global.day}", info)
        promptText?.let { board.printPrompt(it) } // first prompt, then content!
        board.printContent(content)
        text.text = board.convertAndReset()
    }

    private fun AbstracteResource.formatInfo(): String =
        "${labelPlural.capitalize()}: " + when (this) {
            is UsableEntity -> "${usedAmount.formatted} / ${amount.formatted}"
            is LimitedAmount -> "${amount.formatted} / ${limitAmount.formatted}"
            else -> amount.formatted
        }

}


private class Board {

    private val log = logger {}
    private val width = VIEW_SIZE.first
    private val height = VIEW_SIZE.second
    private val rows: MutableList<MutableList<Char>> = 1.rangeTo(height).map {
        1.rangeTo(width).map { ' ' }.toMutableList()
    }.toMutableList()
    private val skipRowsAboveContent = 3
    private var currentContentRow = 0
    private val maxContentRow = height - skipRowsAboveContent

    fun convertAndReset(): String {
        val result = rows.joinToString("\n") { cols ->
            cols.fold("") { acc, col -> "$acc$col" }
        }
        reset()
        return result
    }

    fun printContent(text: String) {
        text.split("\n").forEach { row ->
            printSingleRow(row)
        }
    }

    fun printHeader(left: String, right: String) {
        rows[0].write(left + " ".times(width - left.length - right.length) + right)
        rows[1].writeHr()
    }

    fun printPrompt(text: String) {
        rows[rows.lastIndex - 1].writeHr()
        rows[rows.lastIndex].write(text)
    }

    private fun reset() {
        rows.forEach { it.forEachIndexed { i, _ -> it[i] = ' ' } }
        currentContentRow = 0
    }

    private fun printSingleRow(text: String) {
        val lines = text.splitMaxWidth(width)
        if (lines.size > 1) {
            log.warn { "Oversized row was split into ${lines.size} lines:\n$text" }
        }
        lines.forEach { line ->
            val rowIndex = skipRowsAboveContent + currentContentRow++
            if (rowIndex > maxContentRow) {
                log.warn { "Overfull content! row=$rowIndex, maxContentRow=$maxContentRow, text:\n$text" }
                rows[rows.lastIndex - 2].write("... Overfull content can not be displayed :'-( ...")
            } else {
                rows[rowIndex].write(line)
            }
        }
    }

    private fun MutableList<Char>.writeHr() {
        write("--${"=".times(width - 4)}--")
    }

    private fun MutableList<Char>.write(text: String) {
        require(text.length <= width) {
            "writing text length (${text.length}) exceeds maximum of: $width\nText: $text"
        }
        text.forEachIndexed { index, c ->
            this[index] = c
        }
    }

}

