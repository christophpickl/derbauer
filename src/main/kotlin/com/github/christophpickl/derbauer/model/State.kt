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
    val meta = StateMeta()

    val affordableLand get() = player.gold / prices.landBuy
    val affordableFood get() = player.gold / prices.foodBuy
    val playerPeopleMax get() = player.buildings.houses * buildings.houseCapacity

    override fun toString() = "State{day=$day, screen=${screen.javaClass.simpleName}, buildings=$buildings, player=$player}"

    init {
        reset()
    }

    fun reset() {
        day = 1
        player.reset()
        prices.reset()
        buildings.reset()
        meta.reset()
    }
}

class StateMeta {
    var reproductionRate = 0.0

    fun reset() {
        reproductionRate = 0.1
    }
}
