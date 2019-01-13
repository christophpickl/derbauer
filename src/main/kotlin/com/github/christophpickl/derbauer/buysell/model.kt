package com.github.christophpickl.derbauer.buysell

import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.derbauer.model.amount.LimitedAmount

interface Buyable {
    val buyDescription: String
    var buyPrice: Amount
    val buyPossibleAmount get() = Amount.maxOf(Amount.zero, Model.gold / buyPrice.rounded)
    val effectiveBuyPossibleAmount get() = buyPossibleAmount
}

interface Sellable {
    var sellPrice: Amount
    val sellDescription: String
}

interface BuyAndSellable : Buyable, Sellable {
    fun priceFor(buySell: BuySell) = when (buySell) {
        BuySell.Buy -> buyPrice
        BuySell.Sell -> sellPrice
    }

    fun descriptionFor(buySell: BuySell) = when (buySell) {
        BuySell.Buy -> buyDescription
        BuySell.Sell -> sellDescription
    }
}

interface LimitedBuyableAmount : LimitedAmount, Buyable {
    override val effectiveBuyPossibleAmount get() = Amount.minOf(buyPossibleAmount, capacityLeft)
}

enum class BuySell(val label: String) {
    Buy("buy"),
    Sell("sell")
}
