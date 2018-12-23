package com.github.christophpickl.derbauer.model


class Buildings {
    var farms = 0
    var houses = 0

    val all = listOf(Buildings::farms, Buildings::houses)

    val totalCount get() = all.map { it.get(this) }.sum()

    fun formatAll() = listOf("Houses: $houses", "Farms: $farms")

    override fun toString() = "Buildings{farms=$farms}"
    fun reset() {
        farms = 1
        houses = 1
    }
}

class BuildingsMeta {
    var houseCapacity = 0
    var farmProduces = 0

    fun reset() {
        houseCapacity = 5
        farmProduces = 2
    }

    override fun toString() = "BuildingsMeta{houseCapacity=$houseCapacity, farmProduces=$farmProduces}"
}
