package com.github.christophpickl.derbauer.build

import com.github.christophpickl.derbauer.model.DEPRECATED_CHEAT_MODE
import com.github.christophpickl.derbauer.model.State

data class Buildings(
    var houses: Int = 0,
    var granaries: Int = 0,
    var farms: Int = 0,
    var castles: Int = 0
) {

    val all = listOf(Buildings::houses, Buildings::granaries, Buildings::farms, Buildings::castles)

    val totalCount get() = all.map { it.get(this) }.sum()

    fun formatAll() = mutableListOf("Houses: $houses", "Granaries: $granaries", "Farms: $farms").apply {
        if (State.feature.isCastleEnabled) {
            add("Castles: $castles")
        }
    }

    fun reset() {
        houses = if (DEPRECATED_CHEAT_MODE) 5 else 1
        granaries = if (DEPRECATED_CHEAT_MODE) 8 else 1
        farms = if (DEPRECATED_CHEAT_MODE) 10 else 1
        castles = 0
    }
}

data class BuildingsMeta(
    var houseCapacity: Int = 0,
    var granaryCapacity: Int = 0,
    var farmProduction: Int = 0,
    var castlePeopleCapacity: Int = 0
) {
    fun reset() {
        houseCapacity = 5
        granaryCapacity = 100
        farmProduction = 2
        castlePeopleCapacity = 40
    }
}

class BuildingPrices {
    var house = 0
    var granary = 0
    var farm = 0
    var castle = 0

    fun reset() {
        house = 15
        granary = 30
        farm = 50
        castle = 120
    }

    override fun toString() = "BuildingPrices{house=$house, granary=$granary, farm=$farm, castle=$castle}"
}
