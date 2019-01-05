package com.github.christophpickl.derbauer.upgrade

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.building.FarmProductivityUpgrade
import com.github.christophpickl.derbauer.buysell.Buyable
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.military.MilitaryUpgrade
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Describable
import com.github.christophpickl.derbauer.model.Entity
import com.github.christophpickl.derbauer.model.Ordered
import com.github.christophpickl.derbauer.model.PlayerEntity
import com.github.christophpickl.derbauer.model.filterConditional
import com.github.christophpickl.derbauer.model.ordered
import com.github.christophpickl.derbauer.model.sumBy
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import com.github.christophpickl.kpotpourri.common.string.Stringifier

data class Upgrades(

    val farmProductivity: FarmProductivityUpgrade = FarmProductivityUpgrade(),
    val militaryUpgrade: MilitaryUpgrade = MilitaryUpgrade()

) : PlayerEntity {
    @get:JsonIgnore val all get() = propertiesOfType<Upgrades, Upgrade>(this).ordered().filterConditional()

    override val wealth: Amount
        get() = all.sumBy {
            it.initialBuyPrice * (it.currentLevel * Values.upgrades.increasePriceAfterBought)
        }
}

interface Upgrade : Entity, Describable, Buyable, Ordered {
    var currentLevel: Int
    val maxLevel: Int

    val initialBuyPrice: Amount
    val isMaxLevelReached get() = currentLevel == maxLevel
    fun execute()
}

abstract class AbstractUpgrade(
    override val label: String,
    final override var buyPrice: Amount,
    override val maxLevel: Int
) : Upgrade {

    override val initialBuyPrice = buyPrice
    override var currentLevel = 0
    final override val buyDescription get() = "${buyPrice.formatted} gold"

    companion object {
        var counter = 0
    }

    final override val order = counter++
    override fun toString() = Stringifier.stringify(this)
}

