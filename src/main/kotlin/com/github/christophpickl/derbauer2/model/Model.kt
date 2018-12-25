package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.action.Actions
import com.github.christophpickl.derbauer2.endturn.Notifications
import com.github.christophpickl.derbauer2.endturn.achievement.Achievements
import com.github.christophpickl.derbauer2.feature.Features
import com.github.christophpickl.derbauer2.home.HomeView
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.ui.view.View

object Model : ResourceHolder {

    lateinit var currentView: View
    
    var player: Player = Player()
    var global: Global = Global()
    var history: History = History()
    var features: Features = Features()
    var achievements: Achievements = Achievements()
    var actions: Actions = Actions()
    var notifications: Notifications = Notifications()

    //<editor-fold desc="Shortcuts">
    val peopleCapacityLeft get() = player.resources.people.capacityLeft
    val totalPeopleCapacity get() = player.buildings.totalPeopleCapacity
    val foodCapacityLeft get() = player.resources.food.capacityLeft
    val totalFoodCapacity get() = player.buildings.totalFoodCapacity
    val militaryCapacityLeft get() = player.militaries.militaryCapacityLeft
    val totalMilitaryCapacity get() = player.buildings.totalMilitaryCapacity
    //</editor-fold>

    fun goHome() {
        currentView = HomeView()
    }

    override fun toString() = Stringifier.stringify(this)

}
