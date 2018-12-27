package com.github.christophpickl.derbauer.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.build.PeopleCapacityBuilding
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.trade.BuySell
import com.github.christophpickl.derbauer.trade.LimitedBuyableAmount
import com.github.christophpickl.derbauer.trade.Tradeable
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import com.github.christophpickl.kpotpourri.common.string.Stringifier

data class Resources(
    var gold: GoldResource = GoldResource(),
    var food: FoodResource = FoodResource(),
    var people: PeopleResource = PeopleResource(),
    var land: LandResource = LandResource()
) {
    @JsonIgnore val all = propertiesOfType<Resources, AbstracteResource>(this).ordered()
    @get:JsonIgnore val allTradeables get() = filterAll<TradeableResource>()

    inline fun <reified T : Resource> filterAll() = all.filterIsInstance<T>()
}

interface Resource : Entity, Amountable, MultiLabeled, Ordered

interface TradeableResource : Resource, Tradeable {
    val sellPossible: Int
    var buyPriceModifier: Double
    var sellPriceModifier: Double

    val effectiveBuyPrice get() = (buyPrice * buyPriceModifier).toInt()
    val effectiveSellPrice get() = (sellPrice * sellPriceModifier).toInt()

    override val buyDescription get() = "${effectivePriceFor(BuySell.Buy)} gold"
    override val sellDescription get() = "${effectivePriceFor(BuySell.Sell)} gold"

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

    final override val order = counter++
    override fun toString() = Stringifier.stringify(this)
}

class FoodResource : AbstracteResource(
    labelSingular = "food",
    labelPlural = "food",
    amount = Values.resources.food
), LimitedBuyableAmount, TradeableResource {
    override val limitAmount get() = Model.player.buildings.totalFoodCapacity
    override var buyPrice: Int = Values.resources.foodBuyPrice
    override var sellPrice: Int = Values.resources.foodSellPrice
    override val sellPossible get() = Math.max(0, amount)
    override var buyPriceModifier = 1.0
    override var sellPriceModifier = 1.0
    override fun toString() = Stringifier.stringify(this)
}

class GoldResource : AbstracteResource(
    labelSingular = "gold",
    labelPlural = "gold",
    amount = Values.resources.gold
) {
    override fun toString() = Stringifier.stringify(this)
}

class PeopleResource : AbstracteResource(
    labelSingular = "people",
    labelPlural = "people",
    amount = Values.resources.people
), LimitedAmount {
    override val limitAmount
        get() = Model.player.buildings.filterAll<PeopleCapacityBuilding>().sumBy { it.totalPeopleCapacity }
    override fun toString() = Stringifier.stringify(this)
}

class LandResource : AbstracteResource(
    labelSingular = "land",
    labelPlural = "land",
    amount = Values.resources.land
), UsableEntity, TradeableResource {
    override val usedAmount get() = Model.player.buildings.totalLandNeeded
    override var buyPrice: Int = Values.resources.landBuyPrice
    override var sellPrice: Int = Values.resources.landSellPrice
    override val sellPossible get() = Model.player.resources.land.unusedAmount
    override var buyPriceModifier = 1.0
    override var sellPriceModifier = 1.0
    override fun toString() = Stringifier.stringify(this)
}

interface ResourceHolder {
    var gold
        get() = Model.player.resources.gold.amount
        set(value) {
            Model.player.resources.gold.amount = value
        }
    var food
        get() = Model.player.resources.food.amount
        set(value) {
            Model.player.resources.food.amount = value
        }
    var people
        get() = Model.player.resources.people.amount
        set(value) {
            Model.player.resources.people.amount = value
        }
    var land
        get() = Model.player.resources.land.amount
        set(value) {
            Model.player.resources.land.amount = value
        }
}
