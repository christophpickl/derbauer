package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.misc.ReflectPlayer
import com.github.christophpickl.derbauer2.misc.ReflectPlayerImpl
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.trade.Tradeable
import kotlin.reflect.KMutableProperty1

interface ResourceType : Labeled, ReflectPlayer

object ResourceTypes {
    val gold = GoldResourceType()
    val food = FoodResourceType()
    val people = PeopleResourceType()
    val land = LandResourceType()

    val all = propertiesOfType<ResourceTypes, ResourceType>(this)
    val tradeables = propertiesOfType<ResourceTypes, TradeableResourceType>(this)
}

interface TradeableResourceType : ResourceType, Tradeable {
    val buyPossible: Int
    val sellPossible: Int
}

class FoodResourceType : BaseResourceType(
    labelSingular = "food",
    labelPlural = "food",
    playerProperty = PlayerResources::food
), TradeableResourceType {
    override var buyPrice: Int = 15
    override var sellPrice: Int = 9

    override val buyPossible get() = Model.gold / buyPrice // FIXME granary!
    override val sellPossible get() = Math.max(0, playerRead())

    override fun toString() = Stringifier.stringify(this)
}

class GoldResourceType : BaseResourceType(
    labelSingular = "gold",
    labelPlural = "gold",
    playerProperty = PlayerResources::gold
) {
    override fun toString() = Stringifier.stringify(this)
}

class PeopleResourceType : BaseResourceType(
    labelSingular = "people",
    labelPlural = "people",
    playerProperty = PlayerResources::people
) {
    override fun toString() = Stringifier.stringify(this)
}

class LandResourceType(
) : BaseResourceType(
    labelSingular = "land",
    labelPlural = "land",
    playerProperty = PlayerResources::land
), TradeableResourceType {

    override var buyPrice: Int = 50
    override var sellPrice: Int = 40

    override val buyPossible get() = Model.gold / buyPrice
    override val sellPossible get() = Model.availableLand

    override fun toString() = Stringifier.stringify(this)
}

abstract class BaseResourceType(
    final override val labelSingular: String,
    final override val labelPlural: String,
    playerProperty: KMutableProperty1<PlayerResources, out PlayerResource>,
    reflect: ReflectPlayer = ReflectPlayerImpl(
        host = lazy { Model.player.resources },
        playerProperty = playerProperty
    )
) : ResourceType, ReflectPlayer by reflect
