package com.github.christophpickl.derbauer.endturn.happening.happenings

import com.github.christophpickl.derbauer.data.AsciiArt
import com.github.christophpickl.derbauer.endturn.happening.BaseHappening
import com.github.christophpickl.derbauer.endturn.happening.HappeningNature
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.kpotpourri.common.random.randomListOf

class GoldBagHappening : BaseHappening(
    totalCooldownDays = 7,
    nature = HappeningNature.Positive
) {
    private val minimumGoldBagSize = Amount(100L)

    private fun computeGoldBagSize() =
        Amount.maxOf(minimumGoldBagSize, randomListOf(
            Model.player.relativeWealthBy(0.03) to 50,
            Model.player.relativeWealthBy(0.06) to 35,
            Model.player.relativeWealthBy(0.1) to 15
        ).randomElement())
    
    override fun internalExecute(): String {
        val bagSize = computeGoldBagSize()
        Model.gold += bagSize
        val message = """
            You were lucky.
            
            While walking through the forrest you found a little, dirty bag 
            on the ground next to a big tree. You open it up and ... It contains gold coins!""".trimIndent()
        return "$message\n\n" +
            "${AsciiArt.goldBag}\n\n" +
            "+${bagSize.formatted} gold"
    }
}
