package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.CHEAT_MODE
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Amountable
import com.github.christophpickl.derbauer2.model.Descriptable
import com.github.christophpickl.derbauer2.model.Labeled
import com.github.christophpickl.derbauer2.model.Ordered
import com.github.christophpickl.derbauer2.model.ordered
import com.github.christophpickl.derbauer2.trade.Buyable

interface Building : Labeled, Amountable, Ordered, Buyable, Descriptable {
    var landNeeded: Int

    val totalLandNeeded get() = landNeeded * amount
}

data class PlayerBuildings(
    var houses: HouseBuilding = HouseBuilding(),
    var granaries: GranaryBuilding = GranaryBuilding(),
    var farms: FarmBuilding = FarmBuilding()
) {
    val all = propertiesOfType<PlayerBuildings, Building>(this).ordered()

    inline fun <reified B : Building> allAs(): List<B> = all.mapNotNull { it as? B }
}

interface FoodCapacityBuilding : Building {
    var foodCapacity: Int
    val totalFoodCapacity get() = foodCapacity * amount
}

interface PeopleCapacityBuilding : Building {
    var peopleCapacity: Int
    val totalPeopleCapacity get() = peopleCapacity * amount
}

interface FoodProducerBuilding : Building {
    var producesFood: Int
}

class HouseBuilding : AbstractBuilding(
    labelSingular = "house",
    labelPlural = "houses",
    amount = if (CHEAT_MODE) 5 else 1,
    landNeeded = 1,
    buyPrice = 15
), PeopleCapacityBuilding {
    override var peopleCapacity = 5
    override val descriptionProvider get() = { "adds $peopleCapacity more space for your people" }
}

class GranaryBuilding : AbstractBuilding(
    labelSingular = "granary",
    labelPlural = "granaries",
    landNeeded = 1,
    buyPrice = 30,
    amount = if (CHEAT_MODE) 8 else 1
), FoodCapacityBuilding {
    override var foodCapacity = 100
    override val descriptionProvider get() = { "adds $foodCapacity more food storage" }
}

class FarmBuilding : AbstractBuilding(
    labelSingular = "farm",
    labelPlural = "farms",
    landNeeded = 2,
    buyPrice = 50,
    amount = if (CHEAT_MODE) 10 else 1
), FoodProducerBuilding {
    override var producesFood = 2
    override val descriptionProvider get() = { "produces +$producesFood food each day" }
}

abstract class AbstractBuilding(
    final override val labelSingular: String,
    final override val labelPlural: String,
    override var amount: Int,
    final override var landNeeded: Int,
    final override var buyPrice: Int
) : Building {
    companion object {
        private var counter = 0
    }

    protected abstract val descriptionProvider: () -> String
    final override val description get() = descriptionProvider()
    override val order = counter++
    override fun toString() = Stringifier.stringify(this)
}
