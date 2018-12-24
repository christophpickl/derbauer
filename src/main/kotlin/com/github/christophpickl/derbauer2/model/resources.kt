package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.CHEAT_MODE
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.trade.Buyable
import com.github.christophpickl.derbauer2.trade.Tradeable

data class PlayerResources(
    var gold: GoldResource = GoldResource(),
    var food: FoodResource = FoodResource(),
    var people: PeopleResource = PeopleResource(),
    var land: LandResource = LandResource()
) {
    val all = propertiesOfType<PlayerResources, PlayerResource>(this).ordered()
    val allTradeables = propertiesOfType<PlayerResources, TradeableResourceType>(this).ordered()
}

interface IResource : Amountable, Ordered, Labeled

interface TradeableResourceType : IResource, Tradeable {
    val sellPossible: Int
}

interface LimitedBuyableResource : Buyable, LimitedAmount {
    override val effectiveBuyPossible get() = Math.min(buyPossible, capacityLeft)
}

class FoodResource : PlayerResource(
    labelSingular = "food",
    labelPlural = "food",
    amount = if (CHEAT_MODE) 800 else 300
), LimitedBuyableResource, TradeableResourceType {
    override val limitAmount get() = Model.player.buildings.granaries.totalFoodCapacity
    override var buyPrice: Int = 15
    override var sellPrice: Int = 9
    override val sellPossible get() = Math.max(0, amount)
    override fun toString() = Stringifier.stringify(this)
}

class GoldResource : PlayerResource(
    labelSingular = "gold",
    labelPlural = "gold",
    amount = if (CHEAT_MODE) 500 else 100
) {
    override fun toString() = Stringifier.stringify(this)
}

class PeopleResource : PlayerResource(
    labelSingular = "people",
    labelPlural = "people",
    amount = if (CHEAT_MODE) 9 else 2
), LimitedAmount {
    override val limitAmount get() = Model.player.buildings.houses.totalPeopleCapacity
    override fun toString() = Stringifier.stringify(this)
}

class LandResource : PlayerResource(
    labelSingular = "land",
    labelPlural = "land",
    amount = if (CHEAT_MODE) 100 else 5
), UsableResource, TradeableResourceType {
    override val unusedAmount get() = amount - usedAmount
    override val usedAmount get() = Model.player.buildings.all.sumBy { it.totalLandNeeded }

    override var buyPrice: Int = 50
    override var sellPrice: Int = 40
    override val sellPossible get() = Model.landUnused
    override fun toString() = Stringifier.stringify(this)
}

abstract class PlayerResource(
    override val labelSingular: String,
    override val labelPlural: String,
    override var amount: Int
) : IResource {
    companion object {
        private var counter = 0
    }
    override val order = counter++
    override fun toString() = Stringifier.stringify(this)
}
