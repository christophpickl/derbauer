package com.github.christophpickl.derbauer.model

import com.github.christophpickl.derbauer.logic.multiplyBy
import mu.KotlinLogging.logger

class Prices {
    var house = 0
    var granary = 0
    var farm = 0

    val trade = TradePrices()
    val upgrades = UpgradePrices()

    fun reset() {
        house = 15
        granary = 30
        farm = 50
        trade.reset()
        upgrades.reset()
    }

    override fun toString() = "Prices{house=$house, granary=$granary, farm=$farm, trade=$trade, upgrades=$upgrades}"

}

class TradePrices {
    private val log = logger {}
    var landBuy = 0
    var landSell = 0
    var foodBuy = 0
    var foodSell = 0

    val all = listOf(TradePrices::landBuy, TradePrices::landSell, TradePrices::foodBuy, TradePrices::foodSell)

    private var decreasedBy = 0.0

    fun reset() {
        landBuy = 50
        landSell = 40
        foodBuy = 15
        foodSell = 9
        decreasedBy = 0.0
    }

    fun decreaseAllBy(byFactor: Double) {
        log.debug { "decreaseAllBy(byFactor=$byFactor)" }
        log.trace { "Before: $this" }
        reset()
        decreasedBy = byFactor
        val multiplier = 1.0 - decreasedBy
        all.forEach {
            it.multiplyBy(this, multiplier)
        }
        log.trace { "After: $this" }
    }

    override fun toString() = "TradePrices{landBuy=$landBuy, landSell=$landSell, foodBuy=$foodBuy, foodSell=$foodSell}"
}


class UpgradePrices {
    var farmProductivity = 0

    fun reset() {
        farmProductivity = 250
    }

    override fun toString() = "UpgradePrices{farmProductivity=$farmProductivity}"

}
