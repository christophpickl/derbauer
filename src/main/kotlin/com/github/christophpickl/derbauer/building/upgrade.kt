package com.github.christophpickl.derbauer.building

import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.upgrade.AbstractUpgrade

class FarmProductivityUpgrade : AbstractUpgrade(
    label = "Farm Productivity",
    buyPrice = Values.upgrades.farmProductivityBuyPrice,
    maxLevel = 3
), Conditional {
    override fun checkCondition() = Model.features.upgrade.foodProductivityUpgrade.isEnabled()

    var foodProductionIncrease = Values.upgrades.farmProductionIncrease
    override val description get() = "increases food production by $foodProductionIncrease"

    override fun execute() {
        Model.player.buildings.all.filterIsInstance<FoodProducingBuilding>().forEach {
            it.foodProduction += foodProductionIncrease
        }
        buyPrice *= Values.upgrades.increasePriceAfterBought
    }
}
