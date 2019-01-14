package com.github.christophpickl.derbauer.endturn.happening.happenings

import com.github.christophpickl.derbauer.data.AsciiArt
import com.github.christophpickl.derbauer.endturn.happening.BaseHappening
import com.github.christophpickl.derbauer.endturn.happening.HappeningNature
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.random.randomListOf

class GoldBagHappening : BaseHappening(
    totalCooldownDays = 7,
    nature = HappeningNature.Positive
) {

    private val goldAmounts = listOf(0.03, 0.06, 0.1).map { Model.player.relativeWealthBy(it) }
    private val randomGoldAmounts = randomListOf(
        goldAmounts[0] to 50,
        goldAmounts[1] to 35,
        goldAmounts[2] to 15
    )

    override fun internalExecute(): String {
        val bagSize = randomGoldAmounts.randomElement()
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
