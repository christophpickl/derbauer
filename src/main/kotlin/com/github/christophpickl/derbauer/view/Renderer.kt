package com.github.christophpickl.derbauer.view

import com.github.christophpickl.derbauer.logic.Prompt
import com.github.christophpickl.derbauer.logic.ResourceMeta
import com.github.christophpickl.derbauer.logic.State
import com.github.christophpickl.derbauer.logic.VIEW_SIZE
import com.github.christophpickl.derbauer.logic.formatRightBound
import com.github.christophpickl.derbauer.logic.screens.BuildScreen
import com.github.christophpickl.derbauer.logic.screens.ChooseScreen
import com.github.christophpickl.derbauer.logic.screens.EndTurnScreen
import com.github.christophpickl.derbauer.logic.screens.FoodBuyScreen
import com.github.christophpickl.derbauer.logic.screens.FoodSellScreen
import com.github.christophpickl.derbauer.logic.screens.GameOverScreen
import com.github.christophpickl.derbauer.logic.screens.HomeScreen
import com.github.christophpickl.derbauer.logic.screens.LandBuyScreen
import com.github.christophpickl.derbauer.logic.screens.LandSellScreen
import com.github.christophpickl.derbauer.logic.screens.ScreenCallback
import com.github.christophpickl.derbauer.logic.screens.TradeScreen
import com.github.christophpickl.derbauer.logic.screens.UpgradeScreen
import com.github.christophpickl.kpotpourri.common.string.times
import mu.KotlinLogging.logger
import javax.inject.Inject

class Renderer @Inject constructor(
    private val state: State
) : ScreenCallback {

    private val log = logger {}
    private val board = Board()

    fun render(): String {
        log.debug { "Rendering: $state" }
        val headerStats = listOf(
            Pair("Food", state.player.foodFormatted),
            Pair("People", formatRightBound("${state.player.people}/${state.playerPeopleMax}", ResourceMeta.peopleDigits + 4)),
            Pair("Gold", state.player.goldFormatted),
            Pair("Land", formatRightBound("${state.player.buildings.totalCount}/${state.player.land}", ResourceMeta.landDigits + 3))
        ).joinToString("  ") {
            "${it.first}: ${it.second}"
        }
        board.printHeader("Day: ${state.day}", headerStats)
        renderScreen()
        board.printPrompt(state.prompt)

        return board.convertAndReset()
    }

    private fun renderScreen() {
        board.printRow(state.screen.message)
        board.printRow("")
        state.screen.onCallback(this)
    }

    override fun onHomeScreen(screen: HomeScreen) {
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

    override fun onBuild(screen: BuildScreen) {
        onChooseScreen(screen)
        board.printRow("")
        board.printRow("You've got the following:")
        board.printRow(state.player.buildings.formatAll().joinToString("\n") {
            "  $it"
        })
    }

    override fun onTrade(screen: TradeScreen) {
        onChooseScreen(screen)
    }

    override fun onUpgrade(screen: UpgradeScreen) {
        onChooseScreen(screen)
    }

    override fun onEndTurn(@Suppress("UNUSED_PARAMETER") screen: EndTurnScreen) {
        // no op
    }

    override fun onGameOver(@Suppress("UNUSED_PARAMETER") screen: GameOverScreen) {
        // no op
    }

    private fun onChooseScreen(screen: ChooseScreen<*>) {
        screen.choices.forEachIndexed { index, choice ->
            board.printRow("[${index + 1}] ${choice.label}")
        }
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
        text.split("\n").forEach { row ->
            printSingleRow(row)
        }
    }

    fun printHeader(left: String, right: String) {
        rows[0].write(left + " ".times(width - left.length - right.length) + right)
        rows[1].writeHr()
    }

    fun printPrompt(prompt: Prompt) {
        rows[height - 2].writeHr()
        rows.last().write("> ${prompt.enteredText}⌷")
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
            "writing text length (${text.length}) exceeds maximum of: $width"
        }
        text.forEachIndexed { index, c ->
            this[index] = c
        }
    }

}

