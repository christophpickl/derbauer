package com.github.christophpickl.derbauer.view

import com.github.christophpickl.derbauer.logic.GameState
import com.github.christophpickl.derbauer.logic.Prompt
import com.github.christophpickl.derbauer.logic.VIEW_SIZE
import javax.inject.Inject

class Renderer @Inject constructor(
    private val state: GameState
) {

    fun render(): String {
        val board = Board()

        board.printHeader("Round ${state.round}")
        board.printPrompt(state.prompt)

        return board.convert()
    }

}

private class Board {

    private val rows: MutableList<MutableList<Char>> = 1.rangeTo(VIEW_SIZE.height).map {
        1.rangeTo(VIEW_SIZE.width).map { ' ' }.toMutableList()
    }.toMutableList()

    fun convert() = rows.joinToString("\n") { cols ->
        cols.fold("") { acc, col -> "$acc$col" }
    }

    fun printHeader(text: String) {
        rows.first().write(text)
    }

    fun printPrompt(prompt: Prompt) {
        rows.last().write("> ${prompt.enteredText}⌷")
    }

    private fun MutableList<Char>.write(text: String, startIndex: Int = 0) {
        require(text.length + startIndex < VIEW_SIZE.width)
        text.forEachIndexed { index, c ->
            this[index + startIndex] = c
        }
    }

}

