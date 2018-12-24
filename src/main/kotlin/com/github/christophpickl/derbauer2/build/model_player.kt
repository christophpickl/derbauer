package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.CHEAT_MODE
import com.github.christophpickl.derbauer2.misc.Ordered
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.ordered
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Amountable

data class PlayerBuildings(
    var houses: HouseBuilding = HouseBuilding(),
    var granaries: GranaryBuilding = GranaryBuilding(),
    var farms: FarmBuilding = FarmBuilding()
) {
    val all = propertiesOfType<PlayerBuildings, PlayerBuilding<BuildingType>>(this).ordered()
}

class HouseBuilding : PlayerBuilding<HouseType>(
    type = BuildingTypes.house,
    amount = if (CHEAT_MODE) 5 else 1
) {
    val totalPeopleCapacity get() = type.peopleCapacity * amount
}

class GranaryBuilding : PlayerBuilding<GranaryType>(
    type = BuildingTypes.granary,
    amount = if (CHEAT_MODE) 8 else 1
) {
    val totalFoodCapacity get() = type.foodCapacity * amount
}

class FarmBuilding : PlayerBuilding<FarmType>(
    type = BuildingTypes.farm,
    amount = if (CHEAT_MODE) 10 else 1
)

abstract class PlayerBuilding<T : BuildingType>(
    override var amount: Int,
    val type: T
) : Amountable, Ordered {
    companion object {
        private var counter = 0
    }

    override val order = counter++
    val totalLandNeeded get() = amount * type.landNeeded
    override fun toString() = Stringifier.stringify(this)
}
