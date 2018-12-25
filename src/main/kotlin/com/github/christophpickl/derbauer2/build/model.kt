package com.github.christophpickl.derbauer2.build

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.VALUES
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Amountable
import com.github.christophpickl.derbauer2.model.ConditionalEntity
import com.github.christophpickl.derbauer2.model.Descriptable
import com.github.christophpickl.derbauer2.model.Entity
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.MultiLabeled
import com.github.christophpickl.derbauer2.model.filterConditional
import com.github.christophpickl.derbauer2.model.ordered
import com.github.christophpickl.derbauer2.trade.Buyable

data class Buildings(
    var houses: HouseBuilding = HouseBuilding(),
    var granaries: GranaryBuilding = GranaryBuilding(),
    var farms: FarmBuilding = FarmBuilding(),
    val castles: CastleBuilding = CastleBuilding()
) {
    @get:JsonIgnore
    val all get() = propertiesOfType<Buildings, Building>(this).ordered().filterConditional()
    
    val totalLandNeeded get() = all.sumBy { it.totalLandNeeded }
    val totalFoodCapacity get() = all.filterIsInstance<FoodCapacityBuilding>().sumBy { it.totalFoodCapacity }
    val totalFoodProduction get() = all.filterIsInstance<FoodProducingBuilding>().sumBy { it.totalFoodProduction }
    val totalPeopleCapacity get() = all.filterIsInstance<PeopleCapacityBuilding>().sumBy { it.totalPeopleCapacity }
}

interface Building : Entity, MultiLabeled, Amountable, Buyable, Descriptable {
    var landNeeded: Int
    val totalLandNeeded get() = landNeeded * amount
}

interface FoodCapacityBuilding : Building {
    var foodCapacity: Int
    val totalFoodCapacity get() = foodCapacity * amount
}

interface PeopleCapacityBuilding : Building {
    var peopleCapacity: Int
    val totalPeopleCapacity get() = peopleCapacity * amount
}

interface FoodProducingBuilding : Building {
    var foodProduction: Int
    val totalFoodProduction get() = foodProduction * amount
}

class HouseBuilding : AbstractBuilding(
    labelSingular = "house",
    labelPlural = "houses",
    amount = VALUES.houses,
    landNeeded = 1,
    buyPrice = 15
), PeopleCapacityBuilding {
    override var peopleCapacity = 5
}

class GranaryBuilding : AbstractBuilding(
    labelSingular = "granary",
    labelPlural = "granaries",
    landNeeded = 1,
    buyPrice = 30,
    amount = VALUES.granaries
), FoodCapacityBuilding {
    override var foodCapacity = 100
}

class FarmBuilding : AbstractBuilding(
    labelSingular = "farm",
    labelPlural = "farms",
    landNeeded = 2,
    buyPrice = 50,
    amount = VALUES.farms
), FoodProducingBuilding {
    override var foodProduction = 2
}

class CastleBuilding : AbstractBuilding(
    labelSingular = "castle",
    labelPlural = "castles",
    landNeeded = 4,
    buyPrice = 200,
    amount = VALUES.castles
), FoodCapacityBuilding, PeopleCapacityBuilding, ConditionalEntity {
    override var foodCapacity = 500
    override var peopleCapacity = 50
    override fun checkCondition() = Model.feature.isCastleEnabled
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

    final override val order = counter++

    final override fun description() = listOfNotNull(
        if (this is FoodCapacityBuilding) "stores +$foodCapacity food" else null,
        if (this is PeopleCapacityBuilding) "stores +$peopleCapacity people" else null,
        if (this is FoodProducingBuilding) "produces +$foodProduction food" else null
    ).joinToString(" and ")

    override fun toString() = Stringifier.stringify(this)
}
