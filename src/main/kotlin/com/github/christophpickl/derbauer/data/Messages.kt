package com.github.christophpickl.derbauer.data

import com.github.christophpickl.derbauer.misc.InfiniteList
import mu.KotlinLogging.logger

/**
 * Use the messages as kind of "in-game tutorial".
 *
 * Giving advices/hints/tips.
 * Provide help about game mechanics and usage.
 */
object Messages {

    private val log = logger {}

    private val lists = mutableListOf<InfiniteEndTurnList<String>>()

    private fun buildList(vararg messages: String) = InfiniteEndTurnList(messages.toList()).also {
        lists += it
    }

    val home get() = homeList.current()
    private val homeList = buildList(
        "What can I do for you, master?",
        "Looking like a great day today.",
        "Are we done yet?",
        "Yes, sir?",
        "To serve and die.",
        "Your wish is my command."

    )

    val action get() = actionList.current()
    private val actionList = buildList(
        "Looking for some action?",
        "Come back later, things might have changed until then.",
        "Doing additional stuff might pay off."
    )

    val military get() = militaryList.current()
    private val militaryList = buildList(
        "Wipe them out, all of them.",
        "Asta la vista, baby!",
        "Watch out your upkeep does not get too high.",
        "Any problem can be solved with brute force."
    )

    val trade get() = tradeList.current()
    private val tradeList = buildList(
        "This is the right place for some quick cash.",
        "To buy or not to buy, that's the real question.",
        "Psssst, over here! Looking for \"something\"?"
    )

    val upgrade get() = upgradeList.current()
    private val upgradeList = buildList(
        "Bigger, stronger, faster!",
        "Investment in upgrades always pays off.",
        "Boost efficiency of your economy and military strengths.",
        "You want to play with the big boys? First you need big equipment!"
    )

    val build get() = buildList.current()
    private val buildList = buildList(
        "Buildings are crucial, build more of them.",
        "Construction work makes us happy.",
        "Some buildings reveal new features."
    )

    val dailyReport get() = dailyReportList.current()
    private val dailyReportList = buildList(
        "Your daily report, my lord:",
        "So, this is what happened over night:",
        "May I hand over the morning news, sir?"
    )

    fun onEndTurn() {
        log.debug { "onEndTurn()" }
        lists.forEach { it.onEndTurn() }
    }
}

private class InfiniteEndTurnList<T>(
    elements: List<T>
) {
    private val elements = InfiniteList(elements)
    private var currentElement = this.elements.nextInfinite()
    private var currentElementWasSeen = false
    fun current(): T {
        currentElementWasSeen = true
        return currentElement
    }

    fun onEndTurn() {
        if (currentElementWasSeen) {
            currentElement = elements.nextInfinite()
            currentElementWasSeen = false
        }
    }

}
