package com.github.christophpickl.derbauer.resource

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.building.PeopleCapacityBuilding
import com.github.christophpickl.derbauer.buysell.BuyAndSellable
import com.github.christophpickl.derbauer.buysell.BuySell
import com.github.christophpickl.derbauer.buysell.LimitedBuyableAmount
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Amountable
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Entity
import com.github.christophpickl.derbauer.model.LimitedAmount
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.MultiLabeled
import com.github.christophpickl.derbauer.model.Ordered
import com.github.christophpickl.derbauer.model.PlayerEntity
import com.github.christophpickl.derbauer.model.UsableEntity
import com.github.christophpickl.derbauer.model.ordered
import com.github.christophpickl.derbauer.model.sumBy
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import com.github.christophpickl.kpotpourri.common.string.Stringifier

data class Resources(
    var gold: GoldResource = GoldResource(),
    var food: FoodResource = FoodResource(),
    var people: PeopleResource = PeopleResource(),
    var land: LandResource = LandResource()
) : PlayerEntity {
    
    @JsonIgnore val all = propertiesOfType<Resources, AbstracteResource>(this).ordered()
    @get:JsonIgnore val allTradeables get() = filterAll<BuyAndSellableResource>()

    override val wealth: Amount
        get() = gold.amount +
            food.amount * Values.resources.foodBuyPrice +
            people.amount * 10 +
            land.amount * Values.resources.landBuyPrice

    inline fun <reified T : Resource> filterAll() = all.filterIsInstance<T>()
}

interface Resource : Entity, Amountable, MultiLabeled, Ordered

interface BuyAndSellableResource : Resource, BuyAndSellable, Conditional {
    val sellPossible: Amount
    var buyPriceModifier: Double
    var sellPriceModifier: Double

    val effectiveBuyPrice get() = Amount((buyPrice.rounded * buyPriceModifier).toLong())
    val effectiveSellPrice get() = Amount((sellPrice.rounded * sellPriceModifier).toLong())

    override val buyDescription get() = "${effectivePriceFor(BuySell.Buy).formatted} gold"
    override val sellDescription get() = "${effectivePriceFor(BuySell.Sell).formatted} gold"

    fun effectivePriceFor(buySell: BuySell) = when (buySell) {
        BuySell.Buy -> effectiveBuyPrice
        BuySell.Sell -> effectiveSellPrice
    }
}

abstract class AbstracteResource(
    override val labelSingular: String,
    override val labelPlural: String,
    override var amount: Amount
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
), LimitedBuyableAmount, BuyAndSellableResource {
    override val limitAmount get() = Model.player.buildings.totalFoodCapacity
    override var buyPrice = Values.resources.foodBuyPrice
    override var sellPrice = Values.resources.foodSellPrice
    override val sellPossible get() = Amount.maxOf(Amount.zero, amount)
    override var buyPriceModifier = 1.0
    override var sellPriceModifier = 1.0
    override fun checkCondition() = true
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
), UsableEntity, BuyAndSellableResource {
    override val usedAmount get() = Model.player.buildings.totalLandNeeded
    override var buyPrice = Values.resources.landBuyPrice
    override var sellPrice = Values.resources.landSellPrice
    override val sellPossible get() = Model.player.resources.land.unusedAmount
    override var buyPriceModifier = 1.0
    override var sellPriceModifier = 1.0
    override fun checkCondition() = Model.features.trade.landEnabled.isEnabled()
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
