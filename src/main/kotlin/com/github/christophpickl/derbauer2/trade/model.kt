package com.github.christophpickl.derbauer2.trade

import com.github.christophpickl.derbauer2.model.BuySell
import com.github.christophpickl.derbauer2.model.Buyable

interface Tradeable : Buyable {
    var sellPrice: Int

    // TODO modifier: Double
    fun priceFor(buySell: BuySell): Int = when (buySell) {
        BuySell.Buy -> buyPrice
        BuySell.Sell -> sellPrice
    }
}
