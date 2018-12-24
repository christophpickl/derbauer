package com.github.christophpickl.derbauer.trade

import com.github.christophpickl.derbauer.logic.multiplyBy
import com.github.christophpickl.derbauer.model.State
import mu.KotlinLogging

val State.affordableLand get() = player.gold / prices.trade.landBuy
val State.affordableFood get() = player.gold / prices.trade.foodBuy

class TradePrices {
    private val log = KotlinLogging.logger {}
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
