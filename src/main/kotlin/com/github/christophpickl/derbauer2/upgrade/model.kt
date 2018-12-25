package com.github.christophpickl.derbauer2.upgrade

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.Values
import com.github.christophpickl.derbauer2.build.FoodProducingBuilding
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.ConditionalEntity
import com.github.christophpickl.derbauer2.model.Descriptable
import com.github.christophpickl.derbauer2.model.Entity
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.filterConditional
import com.github.christophpickl.derbauer2.model.ordered
import com.github.christophpickl.derbauer2.trade.Buyable

data class Upgrades(
    val farmProductivity: FarmProductivityUpgrade = FarmProductivityUpgrade(),
    val militaryUpgrade: MilitaryUpgrade = MilitaryUpgrade()
) {
    @get:JsonIgnore val all get() = propertiesOfType<Upgrades, Upgrade>(this).ordered().filterConditional()
}

interface Upgrade : Entity, Descriptable, Buyable {
    var currentLevel: Int
    val maxLevel: Int

    val isMaxLevelReached get() = currentLevel == maxLevel
    fun execute()
}

abstract class AbstractUpgrade(
    override val label: String,
    override var buyPrice: Int,
    override val maxLevel: Int
) : Upgrade {

    override var currentLevel = 0
    final override val buyDescription get() = "$buyPrice gold"

    companion object {
        var counter = 0
    }

    final override val order = counter++
    override fun toString() = Stringifier.stringify(this)
}

class FarmProductivityUpgrade : AbstractUpgrade(
    label = "Farm Productivity",
    buyPrice = Values.upgrades.farmProductivityBuyPrice,
    maxLevel = 3
), ConditionalEntity {
    override fun checkCondition() = Model.feature.upgrade.isFoodProductionUpgradeEnabled

    var foodProductionIncrease = Values.upgrades.farmProductionIncrease
    override val description get() = "increases food production by $foodProductionIncrease"

    override fun execute() {
        Model.player.buildings.all.filterIsInstance<FoodProducingBuilding>().forEach {
            it.foodProduction += foodProductionIncrease
        }
        buyPrice = (buyPrice * Values.upgrades.increasePriceAfterBought).toInt()
    }
}

class MilitaryUpgrade : AbstractUpgrade(
    label = "Military Expertise",
    buyPrice = Values.upgrades.militaryBuyPrice,
    maxLevel = 1
) {
    override val description get() = "enables military actions"
    override fun execute() {} // dynamically checked in feature via this level
}
