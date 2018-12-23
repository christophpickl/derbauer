package com.github.christophpickl.derbauer.logic.service

import com.github.christophpickl.derbauer.logic.screens.Screen
import com.github.christophpickl.derbauer.logic.screens.ScreenCallback
import com.github.christophpickl.derbauer.logic.screens.SimpleMessageScreen
import com.github.christophpickl.derbauer.model.State
import mu.KotlinLogging.logger
import javax.inject.Inject
import kotlin.random.Random

class HappeningScreen(state: State, message: String) : SimpleMessageScreen(state, message) {
    override fun onCallback(callback: ScreenCallback) {
        callback.onHappening(this)
    }
}

class EndTurnHappener @Inject constructor(
    private val state: State
) {

    private val log = logger {}
    private val randomHappenings = listOf(
        GoldBagHappening(state),
        RatsHappening(state)
    )
    private var turnsNothingHappened = 999
    private val baseProb = 90.0

    fun letItHappen(): Screen? {
        // TODO cool down for happening
        val prob = Math.min(baseProb, (baseProb / 10 * turnsNothingHappened))
        log.trace { "Happening probability: $prob (turns quiet: $turnsNothingHappened)" }
        if (nextRandom0To100() < prob) {
            turnsNothingHappened = 0
            val happening = randomHappenings.random()
            return HappeningScreen(state, happening.execute())
        }

        turnsNothingHappened++
        return null
    }

    private fun nextRandom0To100() = Random.nextDouble(0.0, 100.0)
}


private class GoldBagHappening(
    private val state: State
) : Happening() {
    private val goldBagSizes = listOf(10, 20, 50)
    override fun execute(): String {
        val bagSize = goldBagSizes.random()
        state.player.gold += bagSize
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

private class RatsHappening(
    private val state: State
) : Happening() {

    private val eatenSizes = listOf(10, 20, 30)

    override fun execute(): String {
        val foodEaten = eatenSizes.random()
        state.player.food -= foodEaten
        return """
            Oh noes!!!
            
            Some of those nasty rats ate some of your food!
            
                   .---.
              (\./)     \.......-
              >' '<  (__.'""${'"'}${'"'}
              " ` " "
            
            -$foodEaten Food
        """.trimIndent()
    }

}

private abstract class Happening {
    abstract fun execute(): String
}
