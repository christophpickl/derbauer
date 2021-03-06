package com.github.christophpickl.derbauer.building

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.buysell.Buyable
import com.github.christophpickl.derbauer.data.ValueBuilding
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Describable
import com.github.christophpickl.derbauer.model.Entity
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.MultiLabeled
import com.github.christophpickl.derbauer.model.Ordered
import com.github.christophpickl.derbauer.model.PlayerEntity
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.derbauer.model.amount.Amountable
import com.github.christophpickl.derbauer.model.amount.sumBy
import com.github.christophpickl.derbauer.model.filterConditional
import com.github.christophpickl.derbauer.model.ordered
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import com.github.christophpickl.kpotpourri.common.string.Stringifier

data class Buildings(
    var houses: HouseBuilding = HouseBuilding(),
    var farms: FarmBuilding = FarmBuilding(),
    var granaries: GranaryBuilding = GranaryBuilding(),
    val castles: CastleBuilding = CastleBuilding(),
    val barracks: BarrackBuilding = BarrackBuilding()
) : PlayerEntity {
    
    @get:JsonIgnore val all get() = propertiesOfType<Buildings, Building>(this).ordered().filterConditional()

    inline fun <reified T : Building> filterAll() = all.filterIsInstance<T>()

    val totalLandNeeded get() = all.sumBy { it.totalLandNeeded }
    val totalFoodCapacity get() = filterAll<FoodCapacityBuilding>().sumBy { it.totalFoodCapacity }
    val totalFoodProduction get() = filterAll<FoodProducingBuilding>().sumBy { it.totalFoodProduction }
    val totalPeopleCapacity get() = filterAll<PeopleCapacityBuilding>().sumBy { it.totalPeopleCapacity }
    val totalArmyCapacity get() = filterAll<ArmyCapacityBuilding>().sumBy { it.totalArmyCapacity }

    override val wealth: Amount get() = all.sumBy { it.amount * it.buyPrice }
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

interface ArmyCapacityBuilding : Building {
    var armyCapacity: Amount
    val totalArmyCapacity get() = armyCapacity * amount
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
), ArmyCapacityBuilding, Conditional {
    override var armyCapacity = Values.buildings.barrackArmyCapacity
    override fun checkCondition() = Model.features.military.menu.isEnabled()
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
    override val effectiveBuyPossibleAmount
        get() = Amount.minOf(buyPossibleAmount,
            Model.player.resources.land.unusedAmount / landNeeded
        )
    
    protected open val additionalDescription: String? = null
    override val buyDescription get() = "${buyPrice.formatted} gold, ${landNeeded.formatted} land"
    final override val description
        get() = listOfNotNull(
            if (this is FoodCapacityBuilding) "stores +${foodCapacity.formatted} food" else null,
            if (this is PeopleCapacityBuilding) "stores +${peopleCapacity.formatted} people" else null,
            if (this is FoodProducingBuilding) "produces +${foodProduction.formatted} food" else null,
            if (this is ArmyCapacityBuilding) "stores +${armyCapacity.formatted} units" else null,
            additionalDescription
        ).also {
            require(it.isNotEmpty()) {
                "No description could be computed for ${this::class.simpleName}! " +
                    "(not of any known building type, nor an additional description provided)"
            }
        }.joinToString(" and ")


    override fun toString() = Stringifier.stringify(this)
}
