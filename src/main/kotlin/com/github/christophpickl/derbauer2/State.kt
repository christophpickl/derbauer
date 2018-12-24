package com.github.christophpickl.derbauer2

import com.github.christophpickl.derbauer2.domain.HomeScreen
import com.github.christophpickl.derbauer2.misc.Screen
import kotlin.reflect.KMutableProperty1

object State {

    lateinit var screen: Screen
    lateinit var global: Global
    lateinit var player: Player

    fun goHome() {
        screen = HomeScreen()
    }

    fun reset() {
        global = Global()
        screen = HomeScreen()
        player = Player()
    }
}

data class Global(
    var day: Int = 1
)

data class Player(
    val resources: PlayerResources = PlayerResources()
)

data class PlayerResources(
    var food: PlayerResource = PlayerResource(
        type = ResourceTypes.food,
        amount = 40
    ),
    var gold: PlayerResource = PlayerResource(
        type = ResourceTypes.gold,
        amount = 40
    )
)

data class PlayerResource(
    var amount: Int,
    val type: ResourceType
)

object ResourceTypes {
    val food = FoodResourceType()
    val gold = GoldResourceType()

    val all = listOf(food, gold)
    val tradeables = all.mapNotNull { it as? TradeableResourceType }
}

interface TradeableResourceType : ResourceType, Tradeable

class FoodResourceType : TradeableResourceType {
    override val labelSingular = "food"
    override val labelPlural = "food"
    override var buyPrice: Int = 10
    override var sellPrice: Int = 8
    override val playerProperty = PlayerResources::food
}

class GoldResourceType : ResourceType {
    override val labelSingular: String = "gold"
    override val labelPlural: String = "gold"
    override val playerProperty = PlayerResources::gold
}

interface ResourceType : Entity {
    val playerProperty: KMutableProperty1<PlayerResources, PlayerResource>
    fun playerValue() = playerProperty.get(State.player.resources)
    fun change(changeBy: Int) {
        playerProperty.set(State.player.resources, playerValue().apply { amount += changeBy })
    }
}

interface Entity {
    val labelSingular: String
    val labelPlural: String
}

interface Tradeable {
    var buyPrice: Int
    var sellPrice: Int

    fun priceFor(option: TradeOption) = when (option) {
        TradeOption.Buy -> buyPrice
        TradeOption.Sell -> sellPrice
    }
}

enum class TradeOption(val label: String) {
    Buy("Buy"),
    Sell("Sell")
}
