package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.VALUES
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.trade.LimitedBuyableAmount
import com.github.christophpickl.derbauer2.trade.Tradeable

data class Resources(
    var gold: GoldResource = GoldResource(),
    var food: FoodResource = FoodResource(),
    var people: PeopleResource = PeopleResource(),
    var land: LandResource = LandResource()
) {
    val all = propertiesOfType<Resources, AbstracteResource>(this).ordered()
    val allTradeables = all.filterIsInstance<TradeableResource>()
}

interface Resource : Entity, Amountable, MultiLabeled

interface TradeableResource : Resource, Tradeable {
    val sellPossible: Int
    var priceModifier: Double

    val effectiveBuyPrice get() = (buyPrice * priceModifier).toInt()
    val effectiveSellPrice get() = (sellPrice * priceModifier).toInt()
    
    fun effectivePriceFor(buySell: BuySell): Int = when (buySell) {
        BuySell.Buy -> effectiveBuyPrice
        BuySell.Sell -> effectiveSellPrice
    }
}

abstract class AbstracteResource(
    override val labelSingular: String,
    override val labelPlural: String,
    override var amount: Int
) : Resource {
    companion object {
        private var counter = 0
    }
    override val order = counter++
    override fun toString() = Stringifier.stringify(this)
}

class FoodResource : AbstracteResource(
    labelSingular = "food",
    labelPlural = "food",
    amount = VALUES.food
), LimitedBuyableAmount, TradeableResource {
    override val limitAmount get() = Model.player.buildings.totalFoodCapacity
    override var priceModifier = 1.0
    override var buyPrice: Int = 15
    override var sellPrice: Int = 9
    override val sellPossible get() = Math.max(0, amount)
    override fun toString() = Stringifier.stringify(this)
}

class GoldResource : AbstracteResource(
    labelSingular = "gold",
    labelPlural = "gold",
    amount = VALUES.gold
) {
    override fun toString() = Stringifier.stringify(this)
}

class PeopleResource : AbstracteResource(
    labelSingular = "people",
    labelPlural = "people",
    amount = VALUES.people
), LimitedAmount {
    override val limitAmount get() = Model.player.buildings.houses.totalPeopleCapacity
    override fun toString() = Stringifier.stringify(this)
}

class LandResource : AbstracteResource(
    labelSingular = "land",
    labelPlural = "land",
    amount = VALUES.land
), UsableEntity, TradeableResource {
    override val usedAmount get() = Model.player.buildings.totalLandNeeded
    override var priceModifier = 1.0
    override var buyPrice: Int = 50
    override var sellPrice: Int = 40
    override val sellPossible get() = Model.player.resources.land.unusedAmount
    override fun toString() = Stringifier.stringify(this)
}