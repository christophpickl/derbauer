package com.github.christophpickl.derbauer2.upgrade

import com.github.christophpickl.derbauer2.VALUES
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Descriptable
import com.github.christophpickl.derbauer2.model.Labeled
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.Ordered
import com.github.christophpickl.derbauer2.model.ordered
import com.github.christophpickl.derbauer2.trade.Buyable

data class PlayerUpgrades(
    val farmProductivity: FarmProductivityUpgrade = FarmProductivityUpgrade()
) {
    val all: List<Upgrade>
        get() {
            return propertiesOfType<PlayerUpgrades, Upgrade>(this).ordered()
            // check conditional
        }

//    inline fun <reified B : Building> allFiltered(): List<B> = all.mapNotNull { it as? B }
}

interface Upgrade : Ordered, Labeled, Descriptable, Buyable {
    fun execute()
}

class FarmProductivityUpgrade : Upgrade {
    override var buyPrice = VALUES.upgradeFarmBuyPrice

    override val description get() = "increases food production by $foodProductionIncrease"
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
