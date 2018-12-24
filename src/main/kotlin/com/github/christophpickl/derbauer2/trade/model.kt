package com.github.christophpickl.derbauer2.trade

import com.github.christophpickl.derbauer2.model.BuySell
import com.github.christophpickl.derbauer2.model.Model

interface Buyable {
    var buyPrice: Int
    val buyPossibleAmount get() = Math.max(0, Model.gold / buyPrice)
    val effectiveBuyPossibleAmount get() = buyPossibleAmount
}

interface Sellable {
    var sellPrice: Int
}

interface Tradeable : Buyable, Sellable {

    fun priceFor(buySell: BuySell): Int = when (buySell) {
        BuySell.Buy -> buyPrice
        BuySell.Sell -> sellPrice
    }
}
