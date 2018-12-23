package com.github.christophpickl.derbauer.view

import com.github.christophpickl.derbauer.logic.ChooseScreen
import com.github.christophpickl.derbauer.logic.GameState
import com.github.christophpickl.derbauer.logic.LandBuyScreen
import com.github.christophpickl.derbauer.logic.LandSellScreen
import com.github.christophpickl.derbauer.logic.MainScreen
import com.github.christophpickl.derbauer.logic.Prompt
import com.github.christophpickl.derbauer.logic.ScreenCallback
import com.github.christophpickl.derbauer.logic.VIEW_SIZE
import com.github.christophpickl.kpotpourri.common.string.times
import javax.inject.Inject

class Renderer @Inject constructor(
    private val state: GameState
) : ScreenCallback {

    private val board = Board()

    fun render(): String {

        val gold = String.format("%6d", state.player.gold)
        val land = String.format("%4d", state.player.land)
        board.printHeader("Round ${state.round}", "Gold: $gold Land: $land")
        renderScreen()
        board.printPrompt(state.prompt)

        return board.convertAndReset()
    }

    private fun renderScreen() {
        board.printRow(0, state.screen.message)
        state.screen.onCallback(this)
    }

    override fun onMainScreen(screen: MainScreen) {
        onChooseScreen(screen)
    }

    override fun onLandBuy(screen: LandBuyScreen) {
        onNumberInputScreen()
    }

    override fun onLandSell(screen: LandSellScreen) {
        onNumberInputScreen()
    }

    private fun onChooseScreen(screen: ChooseScreen<*>) {
        screen.choices.forEachIndexed { index, choice ->
            board.printRow(2 + index, "[${index + 1}] ${choice.label}")
        }
    }

    private fun onNumberInputScreen() {
        board.printRow(2, "Enter a valid number.")
    }

}

private class Board {

    private val width = VIEW_SIZE.width
    private val height = VIEW_SIZE.height
    private val rows: MutableList<MutableList<Char>> = 1.rangeTo(height).map {
        1.rangeTo(width).map { ' ' }.toMutableList()
    }.toMutableList()

    private val skipRowsAboveContent = 2
    private val skipRowsBelowContent = 2
    private val skipRowsForContent = skipRowsAboveContent + skipRowsBelowContent

    fun convertAndReset(): String {
        val result = rows.joinToString("\n") { cols ->
            cols.fold("") { acc, col -> "$acc$col" }
        }
        rows.forEach { it.forEachIndexed { i, _ -> it[i] = ' ' } }
        return result
    }

    fun printRow(relativeRowIndex: Int, text: String, startIndex: Int = 0) {
        require(relativeRowIndex >= 0 && skipRowsForContent + relativeRowIndex <= height)
        rows[skipRowsAboveContent + relativeRowIndex].write(text, startIndex)
    }

    fun printHeader(left: String, right: String) {
        rows[0].write(left + " ".times(width - left.length - right.length) + right)
        rows[1].writeHr()
    }

    fun printPrompt(prompt: Prompt) {
        rows[height - 2].writeHr()
        rows.last().write("> ${prompt.enteredText}⌷")
    }

    private fun MutableList<Char>.writeHr() {
        write("--${"=".times(width - 4)}--")
    }

    private fun MutableList<Char>.write(text: String, startIndex: Int = 0) {
        require((text.length + startIndex) <= width) {
            "writing text length (${text.length}, start: $startIndex) exceeds maximum of: $width"
        }
        text.forEachIndexed { index, c ->
            this[index + startIndex] = c
        }
    }

}

