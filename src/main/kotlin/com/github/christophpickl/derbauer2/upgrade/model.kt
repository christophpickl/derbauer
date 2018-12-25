package com.github.christophpickl.derbauer2.upgrade

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.Values
import com.github.christophpickl.derbauer2.build.FoodProducingBuilding
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Descriptable
import com.github.christophpickl.derbauer2.model.Entity
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.filterConditional
import com.github.christophpickl.derbauer2.model.ordered
import com.github.christophpickl.derbauer2.trade.Buyable

data class Upgrades(
    val farmProductivity: FarmProductivityUpgrade = FarmProductivityUpgrade()
) {
    @get:JsonIgnore val all get() = propertiesOfType<Upgrades, Upgrade>(this).ordered().filterConditional()
}

interface Upgrade : Entity, Descriptable, Buyable {
    fun execute()
}

abstract class AbstractUpgrade(
    override val label: String,
    override var buyPrice: Int
) : Upgrade {

    final override fun buyDescription() = "$buyPrice gold"

    companion object {
        var counter = 0
    }

    final override val order = counter++
    override fun toString() = Stringifier.stringify(this)
}

class FarmProductivityUpgrade : AbstractUpgrade(
    label = "Farm Productivity",
    buyPrice = Values.upgrades.farmBuyPrice
) {

    var foodProductionIncrease = Values.upgrades.farmProductionIncrease
    override fun description() = "increases food production by $foodProductionIncrease"

    override fun execute() {
        Model.player.buildings.all.filterIsInstance<FoodProducingBuilding>().forEach {
            it.foodProduction += foodProductionIncrease
        }
        buyPrice = (buyPrice * Values.upgrades.increasePriceAfterBought).toInt()
    }
}
