package com.github.christophpickl.derbauer.view

import com.github.christophpickl.derbauer.logic.GameState
import com.github.christophpickl.derbauer.logic.Prompt
import com.github.christophpickl.derbauer.logic.VIEW_SIZE
import com.github.christophpickl.kpotpourri.common.string.times
import javax.inject.Inject

class Renderer @Inject constructor(
    private val state: GameState
) {

    fun render(): String {
        val board = Board()

        val gold = String.format("%6d", state.player.gold)
        val land = String.format("%4d", state.player.land)
        board.printHeader("Round ${state.round}", "Gold: $gold Land: $land")
        board.printPrompt(state.prompt)

        return board.convert()
    }

}

private class Board {

    private val width = VIEW_SIZE.width
    private val height = VIEW_SIZE.height
    private val rows: MutableList<MutableList<Char>> = 1.rangeTo(height).map {
        1.rangeTo(width).map { ' ' }.toMutableList()
    }.toMutableList()

    fun convert() = rows.joinToString("\n") { cols ->
        cols.fold("") { acc, col -> "$acc$col" }
    }

    fun printHeader(left: String, right: String) {
        rows.first().write(left + " ".times(width - left.length - right.length) + right)
    }

    fun printPrompt(prompt: Prompt) {
        rows.last().write("> ${prompt.enteredText}⌷")
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

