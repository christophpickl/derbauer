package com.github.christophpickl.derbauer2.state

import com.github.christophpickl.derbauer2.misc.IgnoreStringified
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import kotlin.reflect.KMutableProperty1

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

class FoodResourceType : TradeableResourceType {
    override val labelSingular = "food"
    override val labelPlural = "food"
    @IgnoreStringified
    override val playerProperty = PlayerResources::food
    override var buyPrice: Int = 15
    override var sellPrice: Int = 9

    override val buyPossible get() = State.gold / buyPrice // TODO granary!
    override val sellPossible get() = Math.max(0, playerRead().amount)

    override fun toString() = Stringifier.stringify(this)
}

class GoldResourceType : ResourceType {
    override val labelSingular: String = "gold"
    override val labelPlural: String = "gold"
    override val playerProperty = PlayerResources::gold
}

class PeopleResourceType : ResourceType {
    override val labelSingular = "people"
    override val labelPlural = "people"
    override val playerProperty = PlayerResources::people
}

class LandResourceType : TradeableResourceType {
    override val labelSingular: String = "land"
    override val labelPlural: String = "land"
    @IgnoreStringified
    override val playerProperty = PlayerResources::land
    override var buyPrice: Int = 50
    override var sellPrice: Int = 40

    override val buyPossible get() = State.gold / buyPrice
    override val sellPossible get() = Math.max(0, playerRead().amount) // TODO calculate available land by subtracting building count (each building has dynamic land consume need)

    override fun toString() = Stringifier.stringify(this)
}

interface ResourceType : Entity {
    val playerProperty: KMutableProperty1<PlayerResources, PlayerResource>
    fun playerRead() = playerProperty.get(State.player.resources)
    fun playerChange(changeBy: Int) {
        playerProperty.set(State.player.resources, playerRead().apply { amount += changeBy })
    }
}
