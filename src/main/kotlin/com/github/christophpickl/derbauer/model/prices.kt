package com.github.christophpickl.derbauer.model

class Prices {
    var landBuy = 0
    var landSell = 0
    var foodBuy = 0
    var foodSell = 0
    var farm = 0
    var house = 0
    val upgrades = UpgradePrices()

    fun reset() {
        landBuy = 20
        landSell = 15
        foodBuy = 4
        foodSell = 2
        farm = 40
        house = 25
        upgrades.reset()
    }
}

class UpgradePrices {
    var farmProductivity = 0

    fun reset() {
        farmProductivity = 250
    }
}
