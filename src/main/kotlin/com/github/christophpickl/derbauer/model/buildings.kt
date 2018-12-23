package com.github.christophpickl.derbauer.model


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
    var farmProduces = 0

    fun reset() {
        houseCapacity = 5
        granaryCapacity = 100
        farmProduces = 2
    }

    override fun toString() = "BuildingsMeta{houseCapacity=$houseCapacity, granaryCapacity=$granaryCapacity, farmProduces=$farmProduces}"
}