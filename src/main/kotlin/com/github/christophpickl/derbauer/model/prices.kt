package com.github.christophpickl.derbauer.model

import com.github.christophpickl.derbauer.army.ArmyPrices
import com.github.christophpickl.derbauer.build.BuildingPrices
import com.github.christophpickl.derbauer.trade.TradePrices
import com.github.christophpickl.derbauer.upgrade.UpgradePrices

@Deprecated(message = "v2")
class Prices {
    val buildings = BuildingPrices()
    val trade = TradePrices()
    val upgrades = UpgradePrices()
    val army = ArmyPrices()

    fun reset() {
        buildings.reset()
        trade.reset()
        upgrades.reset()
        army.reset()
    }

    override fun toString() = "Prices{buildings=$buildings, trade=$trade, upgrades=$upgrades, army=$army}"

}
