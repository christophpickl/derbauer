package com.github.christophpickl.derbauer.build

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.buysell.Buyable
import com.github.christophpickl.derbauer.data.ValueBuilding
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Amountable
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Describable
import com.github.christophpickl.derbauer.model.Entity
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.MultiLabeled
import com.github.christophpickl.derbauer.model.Ordered
import com.github.christophpickl.derbauer.model.filterConditional
import com.github.christophpickl.derbauer.model.ordered
import com.github.christophpickl.derbauer.model.sumBy
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import com.github.christophpickl.kpotpourri.common.string.Stringifier

data class Buildings(
    var houses: HouseBuilding = HouseBuilding(),
    var farms: FarmBuilding = FarmBuilding(),
    var granaries: GranaryBuilding = GranaryBuilding(),
    val castles: CastleBuilding = CastleBuilding(),
    val barracks: BarrackBuilding = BarrackBuilding()
) {
    @get:JsonIgnore val all get() = propertiesOfType<Buildings, Building>(this).ordered().filterConditional()

    inline fun <reified T : Building> filterAll() = all.filterIsInstance<T>()

    val totalLandNeeded get() = all.sumBy { it.totalLandNeeded }
    val totalFoodCapacity get() = filterAll<FoodCapacityBuilding>().sumBy { it.totalFoodCapacity }
    val totalFoodProduction get() = filterAll<FoodProducingBuilding>().sumBy { it.totalFoodProduction }
    val totalPeopleCapacity get() = filterAll<PeopleCapacityBuilding>().sumBy { it.totalPeopleCapacity }
    val totalMilitaryCapacity get() = filterAll<MilitaryCapacityBuilding>().sumBy { it.totalMilitaryCapacity }
}

interface Building : Entity, MultiLabeled, Amountable, Buyable, Describable, Ordered {
    var landNeeded: Amount
    val totalLandNeeded: Amount get() = amount * landNeeded
}

interface FoodCapacityBuilding : Building {
    var foodCapacity: Amount
    val totalFoodCapacity: Amount get() = foodCapacity * amount
}

interface PeopleCapacityBuilding : Building {
    var peopleCapacity: Amount
    val totalPeopleCapacity: Amount get() = peopleCapacity * amount
}


interface FoodProducingBuilding : Building {
    var foodProduction: Amount
    val totalFoodProduction: Amount get() = foodProduction * amount
}

interface MilitaryCapacityBuilding : Building {
    var militaryCapacity: Amount
    val totalMilitaryCapacity get() = militaryCapacity * amount

}

class HouseBuilding : AbstractBuilding(
    labelSingular = "house",
    labelPlural = "houses",
    values = Values.buildings.houses
), PeopleCapacityBuilding {
    override var peopleCapacity = Values.buildings.housePeopleCapacity
}

class FarmBuilding : AbstractBuilding(
    labelSingular = "farm",
    labelPlural = "farms",
    values = Values.buildings.farms
), FoodProducingBuilding {
    override var foodProduction = Values.buildings.farmFoodProduction
}

class GranaryBuilding : AbstractBuilding(
    labelSingular = "granary",
    labelPlural = "granaries",
    values = Values.buildings.granaries
), FoodCapacityBuilding {
    override var foodCapacity = Values.buildings.granaryFoodCapacity
}

class BarrackBuilding : AbstractBuilding(
    labelSingular = "barrack",
    labelPlural = "barracks",
    values = Values.buildings.barrack
), MilitaryCapacityBuilding, Conditional {
    override var militaryCapacity = Values.buildings.barrackMilitaryCapacity
    override fun checkCondition() = Model.features.military.menu.isEnabled()
    override val additionalDescription = "enables new units"
}

class CastleBuilding : AbstractBuilding(
    labelSingular = "castle",
    labelPlural = "castles",
    values = Values.buildings.castles
), FoodCapacityBuilding, PeopleCapacityBuilding, Conditional {
    override var foodCapacity = Values.buildings.castleFoodCapacity
    override var peopleCapacity = Values.buildings.castlePeopleCapacity
    override fun checkCondition() = Model.features.building.castleEnabled.isEnabled()
}

abstract class AbstractBuilding(
    final override val labelSingular: String,
    final override val labelPlural: String,
    values: ValueBuilding
) : Building {
    companion object {
        private var counter = 0
    }

    final override val order = counter++
    final override var amount = values.amount
    final override var landNeeded = values.landNeeded
    final override var buyPrice = values.buyPrice
    protected open val additionalDescription: String? = null
    final override val description
        get() = listOfNotNull(
            if (this is FoodCapacityBuilding) "stores +${foodCapacity.formatted} food" else null,
            if (this is PeopleCapacityBuilding) "stores +${peopleCapacity.formatted} people" else null,
            if (this is FoodProducingBuilding) "produces +${foodProduction.formatted} food" else null,
            if (this is MilitaryCapacityBuilding) "stores +${militaryCapacity.formatted} units" else null,
            additionalDescription
        ).also {
            require(it.isNotEmpty()) {
                "No description could be computed for ${this::class.simpleName}! " +
                    "(not of any known building type, nor an additional description provided)"
            }
        }.joinToString(" and ")

    override val buyDescription get() = "${buyPrice.formatted} gold and ${landNeeded.formatted} land"
    override fun toString() = Stringifier.stringify(this)
}
