package com.github.christophpickl.derbauer.data

import com.github.christophpickl.derbauer.misc.infiniteListOf

/**
 * Use the messages as kind of "in-game tutorial".
 *
 * Giving advices/hints/tips.
 * Provide help about game mechanics and usage.
 */
object Messages {

    val home get() = homeList.nextInfinite()
    private val homeList = infiniteListOf(
        "What can I do for you, master?",
        "Looking like a great day today.",
        "Are we done yet?",
        "Yes, sir?",
        "To serve and die.",
        "Your wish is my command."
    )

    val action get() = actionList.nextInfinite()
    private val actionList = infiniteListOf(
        "Looking for some action?",
        "Come back later, things might have changed until then.",
        "Doing additional stuff might pay off."
    )

    val military get() = militaryList.nextInfinite()
    private val militaryList = infiniteListOf(
        "Wipe them out, all of them.",
        "Asta la vista, baby!",
        "Watch out your upkeep does not get too high.",
        "Any problem can be solved with brute force."
    )

    val trade get() = tradeList.nextInfinite()
    private val tradeList = infiniteListOf(
        "This is the right place for some quick cash.",
        "To buy or not to buy, that's the real question.",
        "Psssst, over here! Looking for \"something\"?"
    )

    val upgrade get() = upgradeList.nextInfinite()
    private val upgradeList = infiniteListOf(
        "Bigger, stronger, faster!",
        "Investment in upgrades always pays off.",
        "Boost efficiency of your economy and military strengths.",
        "You want to play with the big boys? First you need big equipment!"
    )

    val build get() = buildList.nextInfinite()
    private val buildList = infiniteListOf(
        "Buildings are crucial, build more of them.",
        "Construction work makes us happy.",
        "Some buildings reveal new features."
    )
}
