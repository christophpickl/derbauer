package com.github.christophpickl.derbauer.model

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.christophpickl.derbauer.action.Actions
import com.github.christophpickl.derbauer.endturn.achievement.Achievements
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.military.attack.Military
import com.github.christophpickl.derbauer.misc.Features
import com.github.christophpickl.derbauer.misc.Notifications
import com.github.christophpickl.derbauer.resource.ResourceHolder
import com.github.christophpickl.derbauer.ui.view.View
import com.github.christophpickl.kpotpourri.common.string.Stringifier

private val mapper = jacksonObjectMapper()
    .enable(SerializationFeature.INDENT_OUTPUT)
    .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)

object Model : ResourceHolder {

    lateinit var currentView: View

    var player = Player()
    var global = Global()
    var history = History()
    var achievements = Achievements()
    var actions = Actions()
    var notifications = Notifications()
    var military = Military()
    var features = Features() // goes last, requires other to be initialized first

    //<editor-fold desc="Shortcuts">
    val peopleCapacityLeft get() = player.resources.people.capacityLeft
    val totalPeopleCapacity get() = player.buildings.totalPeopleCapacity
    val foodCapacityLeft get() = player.resources.food.capacityLeft
    val totalFoodCapacity get() = player.buildings.totalFoodCapacity
    val foodCapacityRation get() = Percent(Model.food.real.toDouble() / totalFoodCapacity.real.toDouble())
    val armyCapacityLeft get() = player.armies.armyCapacityLeft
    val totalArmyCapacity get() = player.buildings.totalArmyCapacity
    //</editor-fold>

    fun goHome() {
        currentView = HomeView()
    }

    fun toJson(): String = mapper.writeValueAsString(this)

    override fun toString() = Stringifier.stringify(this)

}
