package com.github.christophpickl.derbauer.logic

import mu.KotlinLogging.logger
import javax.inject.Inject
import kotlin.random.Random

class EndTurnHappener @Inject constructor(
    private val state: GameState
) {

    private val log = logger {}
    private val happenings = listOf(
        GoldBagHappening(state),
        RatsHappening(state)
    )
    private var turnsNothingHappened = 999
    private val baseProb = 10.0

    fun letItHappen(): String? {
        val prob = Math.min(baseProb, (baseProb / 10 * turnsNothingHappened))
        log.trace { "Happening probability: $prob (turns quiet: $turnsNothingHappened)" }
        if (nextRandom0To100() < prob) {
            turnsNothingHappened = 0
            val happening = happenings.random()
            return happening.execute()
        }

        turnsNothingHappened++
        return null
    }


    private fun nextRandom0To100() = Random.nextDouble(0.0, 100.0)
}

private class GoldBagHappening(
    private val state: GameState
) : Happening() {
    private val goldBagSizes = listOf(10, 20, 50)
    override fun execute(): String {
        val bagSize = goldBagSizes.random()
        state.player.gold += bagSize
        return "You were lucky, while walking in the forrest you found a treasure: +$bagSize Gold"
    }
}

private class RatsHappening(
    private val state: GameState
) : Happening() {

    private val eatenSizes = listOf(10, 20, 30)

    override fun execute(): String {
        val foodEaten = eatenSizes.random()
        state.player.food -= foodEaten
        return "Oh noes, some smelly rats ate some of your foodz: -$foodEaten Food"
    }

}

private abstract class Happening {
    abstract fun execute(): String
}
