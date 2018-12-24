package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.CHEAT_MODE
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.trade.Tradeable

data class PlayerResources(
    var gold: GoldResource = GoldResource(),
    var food: FoodResource = FoodResource(),
    var people: PeopleResource = PeopleResource(),
    var land: LandResource = LandResource()
) {
    val all = propertiesOfType<PlayerResources, PlayerResource>(this).ordered()
    val allTradeables = propertiesOfType<PlayerResources, TradeableResource>(this).ordered()
}

interface Resource : Amountable, Ordered, Labeled

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

abstract class PlayerResource(
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

class FoodResource : PlayerResource(
    labelSingular = "food",
    labelPlural = "food",
    amount = if (CHEAT_MODE) 800 else 300
), LimitedBuyableAmount, TradeableResource {
    override val limitAmount get() = Model.totalFoodCapacity
    override var priceModifier = 1.0
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
), UsableResource, TradeableResource {
    override val unusedAmount get() = amount - usedAmount
    override val usedAmount get() = Model.player.buildings.all.sumBy { it.totalLandNeeded }
    override var priceModifier = 1.0
    override var buyPrice: Int = 50
    override var sellPrice: Int = 40
    override val sellPossible get() = Model.landUnused
    override fun toString() = Stringifier.stringify(this)
}
