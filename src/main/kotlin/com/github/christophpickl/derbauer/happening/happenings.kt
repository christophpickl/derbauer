package com.github.christophpickl.derbauer.happening

import com.github.christophpickl.derbauer.model.State

class GoldBagHappening : Happening(
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

class RatsHappening : Happening(
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
