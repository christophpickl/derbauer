package com.github.christophpickl.derbauer2.ui

import com.github.christophpickl.derbauer2.model.AbstracteResource
import com.github.christophpickl.derbauer2.model.LimitedAmount
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.UsableEntity
import com.github.christophpickl.kpotpourri.common.string.times

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
        promptText?.let { board.printPrompt(it) }
        board.printContent(content)
        text.text = board.convertAndReset()
    }

    private fun AbstracteResource.formatInfo(): String =
        "$labelPlural: " + when (this) {
            is UsableEntity -> "$usedAmount / $amount"
            is LimitedAmount -> "$amount / $limitAmount"
            else -> amount
        }

}


private class Board {

    private val width = VIEW_SIZE.first
    private val height = VIEW_SIZE.second
    private val rows: MutableList<MutableList<Char>> = 1.rangeTo(height).map {
        1.rangeTo(width).map { ' ' }.toMutableList()
    }.toMutableList()
    private val skipRowsAboveContent = 3
    private var currentContentRow = 0

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
        rows[skipRowsAboveContent + currentContentRow].write(text)
        currentContentRow++
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

