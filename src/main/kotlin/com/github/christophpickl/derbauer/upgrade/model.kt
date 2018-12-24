package com.github.christophpickl.derbauer.upgrade

@Deprecated(message = "v2")
class UpgradePrices {
    var farmProductivity = 0

    fun reset() {
        farmProductivity = 250
    }

    override fun toString() = "UpgradePrices{farmProductivity=$farmProductivity}"

}

@Deprecated(message = "v2")
class UpgradeMeta {
    var increaseFarmProduction = 0

    fun reset() {
        increaseFarmProduction = 1
    }
}
