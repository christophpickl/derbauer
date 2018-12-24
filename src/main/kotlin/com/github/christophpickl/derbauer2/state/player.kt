package com.github.christophpickl.derbauer2.state

import com.github.christophpickl.derbauer2.CHEAT_MODE
import com.github.christophpickl.derbauer2.misc.propertiesOfType

data class Player(
    val resources: PlayerResources = PlayerResources()
)

data class PlayerResources(
    var gold: PlayerResource = PlayerResource(
        type = ResourceTypes.gold,
        amount = if (CHEAT_MODE) 500 else 100,
        order = 1
    ),
    var food: PlayerResource = PlayerResource(
        type = ResourceTypes.food,
        amount = if (CHEAT_MODE) 800 else 300,
        order = 2
    ),
    var people: PlayerResource = PlayerResource(
        type = ResourceTypes.people,
        amount = if (CHEAT_MODE) 9 else 2,
        order = 3
    ),
    var land: PlayerResource = PlayerResource(
        type = ResourceTypes.land,
        amount = if (CHEAT_MODE) 100 else 5,
        order = 4
    )
) {
    val all = propertiesOfType<PlayerResources, PlayerResource>(this).sorted()
}

data class PlayerResource(
    var amount: Int,
    val type: ResourceType,
    val order: Int
) : Comparable<PlayerResource> {

    override fun compareTo(other: PlayerResource) =
        order.compareTo(other.order)

}
