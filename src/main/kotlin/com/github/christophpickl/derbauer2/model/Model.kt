package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.feature.Feature
import com.github.christophpickl.derbauer2.home.HomeScreen
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.ui.screen.Screen

object Model : ResourceHolder {

    var screen: Screen = HomeScreen()
    var global: Global = Global()
    var player: Player = Player()
    var history: History = History()
    var feature: Feature = Feature()

    val peopleCapacityLeft get() = player.resources.people.capacityLeft
    val totalPeopleCapacity get() = player.buildings.totalPeopleCapacity
    val foodCapacityLeft get() = player.resources.food.capacityLeft
    val totalFoodCapacity get() = player.buildings.totalFoodCapacity

    fun goHome() {
        screen = HomeScreen()
    }

    override fun toString() = Stringifier.stringify(this)

}
