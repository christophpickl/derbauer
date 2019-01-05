package com.github.christophpickl.derbauer.endturn.happening.happenings

import com.github.christophpickl.derbauer.data.AsciiArt
import com.github.christophpickl.derbauer.endturn.happening.Happening
import com.github.christophpickl.derbauer.endturn.happening.HappeningNature
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.random.randomListOf

class GoldBagHappening : Happening(
    cooldownDays = 7,
    nature = HappeningNature.Positive
) {
    private val goldAmounts = listOf(0.005, 0.015, 0.03).map { Model.player.relativeWealthBy(it) }
    private val randomGoldAmounts = randomListOf(
        goldAmounts[0] to 60,
        goldAmounts[1] to 30,
        goldAmounts[2] to 10
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
