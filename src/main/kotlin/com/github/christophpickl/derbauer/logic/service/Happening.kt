package com.github.christophpickl.derbauer.logic.service

import com.github.christophpickl.derbauer.logic.screens.Screen
import com.github.christophpickl.derbauer.logic.screens.ScreenCallback
import com.github.christophpickl.derbauer.logic.screens.SimpleMessageScreen
import com.github.christophpickl.derbauer.model.CHEAT_MODE
import com.github.christophpickl.derbauer.model.State
import mu.KotlinLogging.logger
import kotlin.random.Random

class HappeningScreen(message: String) : SimpleMessageScreen(message) {
    override fun onCallback(callback: ScreenCallback) {
        callback.onHappening(this)
    }
}

class EndTurnHappener {

    private val log = logger {}
    private val happenings = listOf(
        GoldBagHappening(),
        RatsHappening()
    )
    private var turnsNothingHappened = 999
    private val baseProb = 10.0

    fun letItHappen(): Screen? {
        happenings.forEach { it.coolUpWarmUp() }

        val prob = if (CHEAT_MODE) 100.0 else Math.min(baseProb, (baseProb / 10 * turnsNothingHappened))
        log.trace { "Happening probability: $prob (turns quiet: $turnsNothingHappened)" }
        if (nextRandom0To100() < prob) {
            turnsNothingHappened = 0
            val happening = happenings.sortedBy { it.currentCooldown }.first()
            return HappeningScreen(happening.execute())
        }

        turnsNothingHappened++
        return null
    }

    private fun nextRandom0To100() = Random.nextDouble(0.0, 100.0)
}


private class GoldBagHappening : Happening(
    cooldownDays = 7
) {
    private val goldBagSizes = listOf(10, 20, 50)
    override fun internalExecute(): String {
        val bagSize = goldBagSizes.random()
        State.player.gold += bagSize
        return """
            You were lucky.
            
            While walking through the forrest you found a little, 
            dirty bag on the path and found some coins in it.
            
              _oOoOoOo_  
             (oOoOoOoOo) 
              )`""${'"'}${'"'}${'"'}`(  
             /         \ 
            | #         |
            \           /
             `=========`
            
            +$bagSize Gold
        """.trimIndent()
    }
}

private class RatsHappening : Happening(
    cooldownDays = 14
) {

    private val eatenSizes = listOf(10, 20, 30) // TODO each of them got different prob

    private val ratAscii = """
           .---.
        (\./)     \.......-
        >' '<  (__.'""${'"'}${'"'}
        " ` " "
        """.trimIndent()

    override fun internalExecute(): String {
        val eatenProposal = eatenSizes.random()
        val (message, foodEaten) = if (State.player.food <= 0) {
            "Lucky you, although there were some rats,\n" +
                "but because there was no food you poor bastard,\n" +
                "you didn't lose any.\n" +
                "One does not have, one can not lose." to 0
        } else {
            "Oh noes!!!\n\n" +
                "Some of those nasty rats ate some of your food!" to Math.min(eatenProposal, State.player.food)
        }
        State.player.food -= foodEaten
        return "$message\n\n" +
            "$ratAscii\n\n" +
            "-$foodEaten Food"
    }

}

private abstract class Happening(
    val cooldownDays: Int
) {

    var currentCooldown = 0

    abstract fun internalExecute(): String

    fun execute(): String {
        currentCooldown = cooldownDays
        return internalExecute()
    }

    fun coolUpWarmUp() {
        if (currentCooldown > 0) {
            currentCooldown--
        }
    }
}
