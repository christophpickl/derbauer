package com.github.christophpickl.derbauer.view

import com.github.christophpickl.derbauer.logic.ChooseScreen
import com.github.christophpickl.derbauer.logic.EndTurnScreen
import com.github.christophpickl.derbauer.logic.FoodBuyScreen
import com.github.christophpickl.derbauer.logic.FoodSellScreen
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
        val headerStats = listOf(
            Pair("Food", state.player.foodFormatted),
            Pair("People", state.player.peopleFormatted),
            Pair("Gold", state.player.goldFormatted),
            Pair("Land", state.player.landFormatted)
        ).joinToString("  ") {
            "${it.first}: ${it.second}"
        }
        board.printHeader("Day: ${state.day}", headerStats)
        renderScreen()
        board.printPrompt(state.prompt)

        return board.convertAndReset()
    }

    private fun renderScreen() {
        state.screen.message.split("\n").forEach { line ->
            board.printRow(line)
        }
        board.printRow("")
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

    override fun onFoodBuy(screen: FoodBuyScreen) {
        onNumberInputScreen()
    }

    override fun onFoodSell(screen: FoodSellScreen) {
        onNumberInputScreen()
    }

    private fun onChooseScreen(screen: ChooseScreen<*>) {
        screen.choices.forEachIndexed { index, choice ->
            board.printRow("[${index + 1}] ${choice.label}")
        }
    }

    override fun onEndTurn(@Suppress("UNUSED_PARAMETER") endTurnScreen: EndTurnScreen) {
        // no-op
    }

    private fun onNumberInputScreen() {
        board.printRow("Enter number")
    }

}

private class Board {

    private val width = VIEW_SIZE.width
    private val height = VIEW_SIZE.height
    private val rows: MutableList<MutableList<Char>> = 1.rangeTo(height).map {
        1.rangeTo(width).map { ' ' }.toMutableList()
    }.toMutableList()

    private val skipRowsAboveContent = 2

    private var currentContentRow = 0
    fun convertAndReset(): String {
        val result = rows.joinToString("\n") { cols ->
            cols.fold("") { acc, col -> "$acc$col" }
        }
        rows.forEach { it.forEachIndexed { i, _ -> it[i] = ' ' } }
        currentContentRow = 0
        return result
    }

    fun printRow(text: String) {
        rows[skipRowsAboveContent + currentContentRow].write(text)
        currentContentRow++
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

    private fun MutableList<Char>.write(text: String) {
        require(text.length <= width) {
            "writing text length (${text.length}) exceeds maximum of: $width"
        }
        text.forEachIndexed { index, c ->
            this[index] = c
        }
    }

}

