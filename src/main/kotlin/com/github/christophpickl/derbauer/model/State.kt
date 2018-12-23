@file:Suppress("ConstantConditionIf")

package com.github.christophpickl.derbauer.model

import com.github.christophpickl.derbauer.logic.Prompt
import com.github.christophpickl.derbauer.logic.screens.Screen

const val CHEAT_MODE = true

class State {

    lateinit var screen: Screen

    var day = 0
    val prompt = Prompt()
    val player = Player()
    val prices = Prices()
    val buildings = BuildingsMeta()
    val army = ArmyMeta()
    val meta = StateMeta()
    val history = History()

    val maxFood get() = player.buildings.granaries * buildings.granaryCapacity
    val freeFood get() = maxFood - player.food
    val maxPeople get() = player.buildings.houses * buildings.houseCapacity
    val freePeople get() = maxPeople - player.people

    override fun toString() = "State{" +
        "day=$day, " +
        "screen=${screen.javaClass.simpleName}, " +
        "buildings=$buildings, " +
        "player=$player, " +
        "prices=$prices, " +
        "meta=$meta, " +
        "history=$history" +
        "}"

    init {
        reset()
    }

    fun reset() {
        day = 1
        player.reset()
        prices.reset()
        buildings.reset()
        army.reset()
        meta.reset()
        history.reset()
    }
}

class StateMeta {
    var reproductionRate = 0.0

    fun reset() {
        reproductionRate = 0.1
    }

    override fun toString() = "StateMeta{reproductionRate=$reproductionRate}"

}

class History {

    var traded = 0
    var attacked = 0

    fun reset() {
        traded = 0
        attacked = 0
    }

    override fun toString() = "History{traded=$traded, attacked=$attacked}"
}
