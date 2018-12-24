package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.CHEAT_MODE
import com.github.christophpickl.derbauer2.misc.Ordered
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.ordered
import com.github.christophpickl.derbauer2.misc.propertiesOfType

data class PlayerResources(
    var food: FoodResource = FoodResource(),
    var gold: GoldResource = GoldResource(),
    var people: PeopleResource = PeopleResource(),
    var land: LandResource = LandResource()
) {
    val all = propertiesOfType<PlayerResources, PlayerResource>(this).ordered()
}

class FoodResource : PlayerResource(
    type = ResourceTypes.food,
    amount = if (CHEAT_MODE) 800 else 300
), LimitedResource {
    override val limitAmount get() = Model.player.buildings.granaries.totalFoodCapacity
}

class GoldResource : PlayerResource(
    type = ResourceTypes.gold,
    amount = if (CHEAT_MODE) 500 else 100
)

class PeopleResource : PlayerResource(
    type = ResourceTypes.people,
    amount = if (CHEAT_MODE) 9 else 2
), LimitedResource {
    override val limitAmount
        get() = Model.player.buildings.houses.type.peopleCapacity *
            Model.player.buildings.houses.amount
}

class LandResource : PlayerResource(
    type = ResourceTypes.land,
    amount = if (CHEAT_MODE) 100 else 5
), UsableResource {
    override val unusedAmount get() = amount - usedAmount
    override val usedAmount get() = Model.player.buildings.all.sumBy { it.totalLandNeeded }
}

abstract class PlayerResource(
    override var amount: Int,
    val type: ResourceType
) : Amountable, Ordered {
    companion object {
        private var counter = 0
    }

    override val order = counter++
    override fun toString() = Stringifier.stringify(this)
}

interface LimitedResource {
    val limitAmount: Int
}

interface UsableResource {
    val unusedAmount: Int
    val usedAmount: Int
}
