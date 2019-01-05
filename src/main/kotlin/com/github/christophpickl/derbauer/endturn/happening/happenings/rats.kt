package com.github.christophpickl.derbauer.endturn.happening.happenings

import com.github.christophpickl.derbauer.data.AsciiArt
import com.github.christophpickl.derbauer.endturn.happening.Happening
import com.github.christophpickl.derbauer.endturn.happening.HappeningNature
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.random.randomListOf

class RatsHappening : Happening(
    cooldownDays = 14,
    nature = HappeningNature.Negative
) {

    private val eatenSizes = listOf(0.001, 0.003, 0.008).map { Model.player.relativeWealthBy(it) }
    private val randomEatenSizes = randomListOf(
        eatenSizes[0] to 60,
        eatenSizes[1] to 30,
        eatenSizes[2] to 10
    )
    
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
