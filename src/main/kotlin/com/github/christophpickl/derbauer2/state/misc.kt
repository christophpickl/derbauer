package com.github.christophpickl.derbauer2.state

data class Global(
    var day: Int = 1
)

interface Entity {
    val labelSingular: String
    val labelPlural: String
}

interface Tradeable {
    var buyPrice: Int
    var sellPrice: Int
    // TODO modifier: Double
    fun priceFor(buySell: BuySell): Int = when (buySell) {
        BuySell.Buy -> buyPrice
        BuySell.Sell -> sellPrice
    }
}

enum class BuySell(label: String) {
    Buy("buy"),
    Sell("bell");

    val labelSmall = label
    val labelCapital = label.capitalize()
}

data class History(
    var traded: Int = 0
)
