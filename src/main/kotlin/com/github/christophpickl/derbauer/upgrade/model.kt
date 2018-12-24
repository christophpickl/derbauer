package com.github.christophpickl.derbauer.upgrade


class UpgradePrices {
    var farmProductivity = 0

    fun reset() {
        farmProductivity = 250
    }

    override fun toString() = "UpgradePrices{farmProductivity=$farmProductivity}"

}

class UpgradeMeta {
    var increaseFarmProduction = 0

    fun reset() {
        increaseFarmProduction = 1
    }
}
