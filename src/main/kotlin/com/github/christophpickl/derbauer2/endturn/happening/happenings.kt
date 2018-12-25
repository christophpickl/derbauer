package com.github.christophpickl.derbauer2.endturn.happening

import com.github.christophpickl.derbauer2.misc.randomListOf
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.AsciiArt

// TODO make bag sizes depend on current Model resource

class GoldBagHappening : Happening(
    cooldownDays = 7
) {
    private val goldBagSizes = randomListOf(
        10 to 60,
        20 to 30,
        50 to 10
    )

    override fun internalExecute(): String {
        val bagSize = goldBagSizes.randomElement()
        Model.gold += bagSize
        val message = """
            You were lucky.
            
            While walking through the forrest you found a little, 
            dirty bag on the path and found some coins in it.""".trimIndent()
        return "$message\n\n" +
            "${AsciiArt.goldBag}\n\n" +
            "+$bagSize Gold"
    }
}


class RatsHappening : Happening(
    cooldownDays = 14
) {

    private val eatenSizes = randomListOf(
        10 to 60,
        20 to 30,
        30 to 10
    )
    
    override fun internalExecute(): String {
        val eatenProposal = eatenSizes.randomElement()
        val (message, foodEaten) = if (Model.food <= 0) {
            """
                Lucky you, although there were some rats,
                but because there was no food you poor bastard,
                you didn't lose any.
                One does not have, one can not lose.
            """.trimIndent() to 0
        } else {
            """
                Oh noes!!!
                
                Some of those nasty rats ate some of your food!
            """.trimIndent() to Math.min(eatenProposal, Model.food)
        }
        Model.food -= foodEaten
        return "$message\n\n" +
            "${AsciiArt.rat}\n\n" +
            "-$foodEaten Food"
    }

}
