package com.github.christophpickl.derbauer2.build

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.ValueBuilding
import com.github.christophpickl.derbauer2.Values
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
    val castles: CastleBuilding = CastleBuilding(),
    val barracks: BarrackBuilding = BarrackBuilding()
) {
    @get:JsonIgnore val all get() = propertiesOfType<Buildings, Building>(this).ordered().filterConditional()

    val totalLandNeeded get() = all.sumBy { it.totalLandNeeded }
    val totalFoodCapacity get() = all.filterIsInstance<FoodCapacityBuilding>().sumBy { it.totalFoodCapacity }
    val totalFoodProduction get() = all.filterIsInstance<FoodProducingBuilding>().sumBy { it.totalFoodProduction }
    val totalPeopleCapacity get() = all.filterIsInstance<PeopleCapacityBuilding>().sumBy { it.totalPeopleCapacity }
    val totalMilitaryCapacity get() = all.filterIsInstance<MilitaryCapacityBuilding>().sumBy { it.totalMilitaryCapacity }
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

interface MilitaryCapacityBuilding : Building {
    var militaryCapacity: Int
    val totalMilitaryCapacity get() = militaryCapacity * amount

}

class HouseBuilding : AbstractBuilding(
    labelSingular = "house",
    labelPlural = "houses",
    values = Values.buildings.houses
), PeopleCapacityBuilding {
    override var peopleCapacity = Values.buildings.housePeopleCapacity
}

class GranaryBuilding : AbstractBuilding(
    labelSingular = "granary",
    labelPlural = "granaries",
    values = Values.buildings.granaries
), FoodCapacityBuilding {
    override var foodCapacity = Values.buildings.granaryFoodCapacity
}

class FarmBuilding : AbstractBuilding(
    labelSingular = "farm",
    labelPlural = "farms",
    values = Values.buildings.farms
), FoodProducingBuilding {
    override var foodProduction = Values.buildings.farmFoodProduction
}

class BarrackBuilding : AbstractBuilding(
    labelSingular = "barrack",
    labelPlural = "barracks",
    values = Values.buildings.barrack
), MilitaryCapacityBuilding, ConditionalEntity {
    override var militaryCapacity = Values.buildings.barrackMilitaryCapacity
    override fun checkCondition() = Model.feature.military.isMilitaryEnabled
}

class CastleBuilding : AbstractBuilding(
    labelSingular = "castle",
    labelPlural = "castles",
    values = Values.buildings.castles
), FoodCapacityBuilding, PeopleCapacityBuilding, ConditionalEntity {
    override var foodCapacity = Values.buildings.castleFoodCapacity
    override var peopleCapacity = Values.buildings.castlePeopleCapacity
    override fun checkCondition() = Model.feature.building.isCastleEnabled
}

abstract class AbstractBuilding(
    final override val labelSingular: String,
    final override val labelPlural: String,
    values: ValueBuilding
) : Building {
    companion object {
        private var counter = 0
    }

    final override var amount = values.amount
    final override var landNeeded = values.landNeeded
    final override var buyPrice = values.buyPrice

    final override val order = counter++

    // TODO support additional description for subtypes
    final override val description
        get() = listOfNotNull(
            if (this is FoodCapacityBuilding) "stores +$foodCapacity food" else null,
            if (this is PeopleCapacityBuilding) "stores +$peopleCapacity people" else null,
            if (this is FoodProducingBuilding) "produces +$foodProduction food" else null,
            if (this is MilitaryCapacityBuilding) "stores +$militaryCapacity units" else null
        ).also { require(it.isNotEmpty()) { "No description could be computed for ${this::class.simpleName}!" } }.joinToString(" and ")

    override val buyDescription get() = "$buyPrice gold and $landNeeded land"
    override fun toString() = Stringifier.stringify(this)
}
