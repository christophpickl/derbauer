package com.github.christophpickl.derbauer.view

import com.github.christophpickl.derbauer.logic.VIEW_SIZE
import com.github.christophpickl.derbauer.logic.formatRightBound
import com.github.christophpickl.derbauer.logic.screens.ArmyScreen
import com.github.christophpickl.derbauer.logic.screens.BuildScreen
import com.github.christophpickl.derbauer.logic.screens.ChooseScreen
import com.github.christophpickl.derbauer.logic.screens.FoodBuyScreen
import com.github.christophpickl.derbauer.logic.screens.FoodSellScreen
import com.github.christophpickl.derbauer.logic.screens.GameOverScreen
import com.github.christophpickl.derbauer.logic.screens.HireSoldiersScreen
import com.github.christophpickl.derbauer.logic.screens.HomeScreen
import com.github.christophpickl.derbauer.logic.screens.LandBuyScreen
import com.github.christophpickl.derbauer.logic.screens.LandSellScreen
import com.github.christophpickl.derbauer.logic.screens.ScreenCallback
import com.github.christophpickl.derbauer.logic.screens.TradeScreen
import com.github.christophpickl.derbauer.logic.screens.UpgradeScreen
import com.github.christophpickl.derbauer.logic.service.AchievementScreen
import com.github.christophpickl.derbauer.logic.service.EndTurnScreen
import com.github.christophpickl.derbauer.logic.service.HappeningScreen
import com.github.christophpickl.derbauer.logic.service.Prompt
import com.github.christophpickl.derbauer.model.ResourceFormats
import com.github.christophpickl.derbauer.model.State
import com.github.christophpickl.kpotpourri.common.string.times
import mu.KotlinLogging.logger

@Suppress("UNUSED_PARAMETER")
class Renderer : ScreenCallback {

    private val log = logger {}
    private val board = Board()

    fun render(): String {
        log.debug { "render()" }
        val headerStats = listOf(
            Pair("Food", formatRightBound("${State.player.food}/${State.maxFood}", ResourceFormats.peopleDigits + 5)),
            Pair("People", formatRightBound("${State.player.people}/${State.maxPeople}", ResourceFormats.peopleDigits + 4)),
            Pair("Gold", State.player.goldFormatted),
            Pair("Land", formatRightBound("${State.player.buildings.totalCount}/${State.player.land}", ResourceFormats.landDigits + 3))
        ).joinToString("  ") {
            "${it.first}: ${it.second}"
        }
        board.printHeader("Day: ${State.day}", headerStats)
        renderScreen()
        if (State.screen.promptEnabled) {
            board.printPrompt(State.prompt)
        }

        return board.convertAndReset()
    }

    private fun renderScreen() {
        board.printRow(State.screen.message)
        board.printRow("")
        State.screen.onCallback(this)
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
    }

    override fun onTrade(screen: TradeScreen) {
        onChooseScreen(screen)
    }

    override fun onUpgrade(screen: UpgradeScreen) {
        onChooseScreen(screen)
    }

    override fun onArmy(screen: ArmyScreen) {
        onChooseScreen(screen)
        board.printRow("")
        board.printRow("You've got the following armies:")
        board.printRow(State.player.armies.formatAll().joinToString("\n") {
            "  $it"
        })
    }

    override fun onHireSoldiers(screen: HireSoldiersScreen) {}

    override fun onAchievement(screen: AchievementScreen) {}
    override fun onEndTurn(screen: EndTurnScreen) {}
    override fun onHappening(screen: HappeningScreen) {}
    override fun onGameOver(screen: GameOverScreen) {}

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

    private val skipRowsAboveContent = 3
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
            "writing text length (${text.length}) exceeds maximum of: $width\nText: $text"
        }
        text.forEachIndexed { index, c ->
            this[index] = c
        }
    }

}

