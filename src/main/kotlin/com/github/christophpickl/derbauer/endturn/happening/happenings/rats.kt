package com.github.christophpickl.derbauer.endturn.happening.happenings

import com.github.christophpickl.derbauer.data.AsciiArt
import com.github.christophpickl.derbauer.endturn.happening.BaseHappening
import com.github.christophpickl.derbauer.endturn.happening.HappeningNature
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.kpotpourri.common.random.randomListOf
import mu.KotlinLogging.logger

class RatsHappening : BaseHappening(
    totalCooldownDays = 14,
    nature = HappeningNature.Negative
) {

    private val log = logger {}
    private val eatenSizes = listOf(0.01, 0.02, 0.04).map { Model.player.relativeWealthBy(it) }
    private val randomEatenSizes = randomListOf(
        eatenSizes[0] to 60,
        eatenSizes[1] to 30,
        eatenSizes[2] to 10
    )

    override fun additionalProbability(): Double {
        val probabilityBasedOnFoodRation = Model.foodCapacityRation.value * 2.0
        log.trace { "probabilityBasedOnFoodRation: $probabilityBasedOnFoodRation (food capacity ration: ${Model.foodCapacityRation})" }
        return probabilityBasedOnFoodRation
    }
    
    override fun internalExecute(): String {
        val eatenProposal = randomEatenSizes.randomElement()
        val (message, foodEaten) = if (Model.food <= 0) {
            """
                Lucky you, although there were some rats,
                but because there was no food you poor bastard,
                you didn't lose any.
                One does not have, one can not lose.
            """.trimIndent() to Amount.zero
        } else {
            """
                Oh noes!!!
                
                Some of those nasty rats ate some of your food!
            """.trimIndent() to Amount.minOf(eatenProposal, Model.food)
        }

        Model.food -= foodEaten
        return "$message\n\n" +
            "${AsciiArt.rat}\n\n" +
            "-${foodEaten.formatted} Food"
    }

}
