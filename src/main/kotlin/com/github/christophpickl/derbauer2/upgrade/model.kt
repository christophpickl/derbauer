package com.github.christophpickl.derbauer2.upgrade

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.VALUES
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Descriptable
import com.github.christophpickl.derbauer2.model.Entity
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.ordered
import com.github.christophpickl.derbauer2.trade.Buyable

data class Upgrades(
    val farmProductivity: FarmProductivityUpgrade = FarmProductivityUpgrade()
) {
    @get:JsonIgnore
    val all: List<Upgrade>
        get() {
            return propertiesOfType<Upgrades, Upgrade>(this).ordered()
            // check conditional
        }

}

interface Upgrade : Entity, Descriptable, Buyable {
    fun execute()
}

class FarmProductivityUpgrade : Upgrade {
    override var buyPrice = VALUES.upgradeFarmBuyPrice

    override fun description() = "increases food production by $foodProductionIncrease"
    override val label = "Farm Productivity"
    override val order = 0

    override fun execute() {
        Model.player.buildings.farms.foodProduction += foodProductionIncrease
        Model.player.buildings.farms.buyPrice += VALUES.upgradeFarmIncreaseFarmBuyPrice
        buyPrice = (buyPrice * VALUES.upgradeIncreasePriceAfterBought).toInt()
    }

    var foodProductionIncrease = VALUES.upgradeFarmProductionIncrease

    override fun toString() = Stringifier.stringify(this)
}
