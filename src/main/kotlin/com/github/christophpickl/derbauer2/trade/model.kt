package com.github.christophpickl.derbauer2.trade

import com.github.christophpickl.derbauer2.model.LimitedAmount
import com.github.christophpickl.derbauer2.model.Model

interface Buyable {
    fun buyDescription(): String
    var buyPrice: Int
    val buyPossibleAmount get() = Math.max(0, Model.gold / buyPrice)
    val effectiveBuyPossibleAmount get() = buyPossibleAmount
}

interface Sellable {
    var sellPrice: Int
    fun sellDescription(): String
}

interface Tradeable : Buyable, Sellable {
    fun priceFor(buySell: BuySell) = when (buySell) {
        BuySell.Buy -> buyPrice
        BuySell.Sell -> sellPrice
    }

    fun descriptionFor(buySell: BuySell) = when (buySell) {
        BuySell.Buy -> buyDescription()
        BuySell.Sell -> sellDescription()
    }
}

interface LimitedBuyableAmount : LimitedAmount, Buyable {
    override val effectiveBuyPossibleAmount get() = Math.min(buyPossibleAmount, capacityLeft)
}

enum class BuySell(val label: String) {
    Buy("buy"),
    Sell("sell")
}
