package com.github.christophpickl.derbauer2.endturn.happening

import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.AsciiArt

// MINOR each bag sizes got different prob (gold+rat)

class GoldBagHappening : Happening(
    cooldownDays = 7
) {
    private val goldBagSizes = listOf(10, 20, 50)

    override fun internalExecute(): String {
        val bagSize = goldBagSizes.random()
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

    private val eatenSizes = listOf(10, 20, 30)

    override fun internalExecute(): String {
        val eatenProposal = eatenSizes.random()
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
