package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.VALUES
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Amountable
import com.github.christophpickl.derbauer2.model.Descriptable
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.MultiLabeled
import com.github.christophpickl.derbauer2.model.Ordered
import com.github.christophpickl.derbauer2.model.ordered
import com.github.christophpickl.derbauer2.trade.Buyable

interface Building : MultiLabeled, Amountable, Ordered, Buyable, Descriptable {
    var landNeeded: Int

    val totalLandNeeded get() = landNeeded * amount
}

data class PlayerBuildings(
    var houses: HouseBuilding = HouseBuilding(),
    var granaries: GranaryBuilding = GranaryBuilding(),
    var farms: FarmBuilding = FarmBuilding(),
    val castles: CastleBuilding = CastleBuilding()
) {
    val all: List<Building>
        get() {
            val reallyAll = propertiesOfType<PlayerBuildings, Building>(this).ordered()
            return reallyAll.filter {
                if (it is ConditionalBuilding) {
                    it.checkCondition()
                } else {
                    true
                }
            }
        }

    inline fun <reified B : Building> allFiltered(): List<B> = all.mapNotNull { it as? B }

    val totalFoodCapacity get() = allFiltered<FoodCapacityBuilding>().sumBy { it.totalFoodCapacity }
    val totalFoodProduction get() = allFiltered<FoodProducingBuilding>().sumBy { it.totalFoodProduction }
    val totalPeopleCapacity get() = allFiltered<PeopleCapacityBuilding>().sumBy { it.totalPeopleCapacity }
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

interface ConditionalBuilding {
    fun checkCondition(): Boolean
}

class HouseBuilding : AbstractBuilding(
    labelSingular = "house",
    labelPlural = "houses",
    amount = VALUES.houses,
    landNeeded = 1,
    buyPrice = 15
), PeopleCapacityBuilding {
    override var peopleCapacity = 5
    override val descriptionProvider get() = { "stores +$peopleCapacity people" }
}

class GranaryBuilding : AbstractBuilding(
    labelSingular = "granary",
    labelPlural = "granaries",
    landNeeded = 1,
    buyPrice = 30,
    amount = VALUES.granaries
), FoodCapacityBuilding {
    override var foodCapacity = 100
    override val descriptionProvider get() = { "stores +$foodCapacity food" }
}

class FarmBuilding : AbstractBuilding(
    labelSingular = "farm",
    labelPlural = "farms",
    landNeeded = 2,
    buyPrice = 50,
    amount = VALUES.farms
), FoodProducingBuilding {
    override var foodProduction = 2
    override val descriptionProvider get() = { "produces +$foodProduction food" }
}

class CastleBuilding : AbstractBuilding(
    labelSingular = "castle",
    labelPlural = "castles",
    landNeeded = 4,
    buyPrice = 200,
    amount = VALUES.castles
), FoodCapacityBuilding, PeopleCapacityBuilding, ConditionalBuilding {
    override var foodCapacity = 500
    override var peopleCapacity = 50
    override fun checkCondition() = Model.feature.isCastleEnabled
    override val descriptionProvider get() = { "stores +$foodCapacity food and and stores +$peopleCapacity people" }
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
