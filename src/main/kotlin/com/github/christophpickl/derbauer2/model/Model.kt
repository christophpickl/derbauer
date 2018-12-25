package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.feature.Feature
import com.github.christophpickl.derbauer2.home.HomeView
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.ui.view.View

object Model : ResourceHolder {

    var currentView: View = HomeView()
    
    var player: Player = Player()
    var global: Global = Global()
    var history: History = History()
    var feature: Feature = Feature()

    val peopleCapacityLeft get() = player.resources.people.capacityLeft
    val totalPeopleCapacity get() = player.buildings.totalPeopleCapacity
    val foodCapacityLeft get() = player.resources.food.capacityLeft
    val totalFoodCapacity get() = player.buildings.totalFoodCapacity

    fun goHome() {
        currentView = HomeView()
    }

    override fun toString() = Stringifier.stringify(this)

}
