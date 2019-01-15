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
    private val minimumFoodEaten = Amount(20L)

    override fun additionalProbability(): Double {
        val probabilityBasedOnFoodRation = Model.foodCapacityRation.value * 2.0
        log.trace { "probabilityBasedOnFoodRation: $probabilityBasedOnFoodRation (food capacity ration: ${Model.foodCapacityRation})" }
        return probabilityBasedOnFoodRation
    }

    private fun computeEatenFood() =
        randomListOf(
            Model.food.percentageOf(0.1) to 60,
            Model.food.percentageOf(0.2) to 30,
            Model.food.percentageOf(0.4) to 10
        ).randomElement().coerceBetween(
            lower = minimumFoodEaten,
            upper = Model.food
        )

    override fun internalExecute(): String {
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
            """.trimIndent() to computeEatenFood()
        }

        Model.food -= foodEaten
        return "$message\n\n" +
            "${AsciiArt.rat}\n\n" +
            "-${foodEaten.formatted} Food"
    }

}
