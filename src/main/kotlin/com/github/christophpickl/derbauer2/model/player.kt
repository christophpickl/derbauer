package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.CHEAT_MODE
import com.github.christophpickl.derbauer2.build.BuildingType
import com.github.christophpickl.derbauer2.build.BuildingTypes
import com.github.christophpickl.derbauer2.misc.propertiesOfType

data class Player(
    val resources: PlayerResources = PlayerResources(),
    val buildings: PlayerBuildings = PlayerBuildings()
)

data class PlayerResources(
    var food: PlayerResource = PlayerResource(
        type = ResourceTypes.food,
        amount = if (CHEAT_MODE) 800 else 300
    ),
    var gold: PlayerResource = PlayerResource(
        type = ResourceTypes.gold,
        amount = if (CHEAT_MODE) 500 else 100
    ),
    var people: PlayerResource = PlayerResource(
        type = ResourceTypes.people,
        amount = if (CHEAT_MODE) 9 else 2
    ),
    var land: PlayerResource = PlayerResource(
        type = ResourceTypes.land,
        amount = if (CHEAT_MODE) 100 else 5
    )
) {
    val all = propertiesOfType<PlayerResources, PlayerResource>(this).sorted()
}

data class PlayerResource(
    override var amount: Int,
    val type: ResourceType
) : Amountable, Comparable<PlayerResource> {
    // MINOR make counter reusable
    companion object {
        private var counter = 0
    }

    private val order = counter++
    override fun compareTo(other: PlayerResource) =
        order.compareTo(other.order)
}

data class PlayerBuildings(
    var houses: PlayerBuilding = PlayerBuilding(
        type = BuildingTypes.house,
        amount = if (CHEAT_MODE) 5 else 1
    )
    // TODO more houses
//        granaries = if (CHEAT_MODE) 8 else 1
//        farms = if (CHEAT_MODE) 10 else 1
//        castles = 0
) {
    val all = propertiesOfType<PlayerBuildings, PlayerBuilding>(this).sorted()
}

data class PlayerBuilding(
    override var amount: Int,
    val type: BuildingType
) : Amountable, Comparable<PlayerBuilding> {
    companion object {
        private var counter = 0
    }

    val totalLandNeeded get() = amount * type.landNeeded

    private val order = counter++
    override fun compareTo(other: PlayerBuilding) =
        order.compareTo(other.order)
}
