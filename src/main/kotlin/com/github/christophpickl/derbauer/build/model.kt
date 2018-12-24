package com.github.christophpickl.derbauer.build

class Buildings {
    var houses = 0
    var granaries = 0
    var farms = 0

    val all = listOf(Buildings::houses, Buildings::granaries, Buildings::farms)

    val totalCount get() = all.map { it.get(this) }.sum()

    fun formatAll() = listOf("Houses: $houses", "Granaries: $granaries", "Farms: $farms")

    fun reset() {
        houses = 1
        granaries = 1
        farms = 1
    }

    override fun toString() = "Buildings{${all.map { "${it.name}=${it.get(this)}" }}}"
}

class BuildingsMeta {
    var houseCapacity = 0
    var granaryCapacity = 0
    var farmProduction = 0

    fun reset() {
        houseCapacity = 5
        granaryCapacity = 100
        farmProduction = 2
    }

    override fun toString() = "BuildingsMeta{houseCapacity=$houseCapacity, granaryCapacity=$granaryCapacity, farmProduces=$farmProduction}"
}

class BuildingPrices {
    var house = 0
    var granary = 0
    var farm = 0

    fun reset() {
        house = 15
        granary = 30
        farm = 50
    }

    override fun toString() = "BuildingPrices{house=$house, granary=$granary, farm=$farm}"
}
