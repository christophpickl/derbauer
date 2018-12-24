package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.HomeScreen
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.ui.screen.Screen
import mu.KotlinLogging.logger

object Model {

    private val log = logger {}
    
    lateinit var screen: Screen
    lateinit var global: Global
    lateinit var player: Player
    lateinit var history: History

    fun reset() {
        log.info { "reset()" }
        screen = HomeScreen()
        global = Global()
        player = Player()
        history = History()
    }
    
    //<editor-fold desc="resource shortcuts">
    var food
        get() = player.resources.food.amount
        set(value) {
            player.resources.food.amount = value
        }
    var gold
        get() = player.resources.gold.amount
        set(value) {
            player.resources.gold.amount = value
        }
    var people
        get() = player.resources.people.amount
        set(value) {
            player.resources.people.amount = value
        }
    var land
        get() = player.resources.land.amount
        set(value) {
            player.resources.land.amount = value
        }

    val peopleCapacityLeft get() = player.resources.people.capacityLeft
    val totalPeopleCapacity get() = player.buildings.totalPeopleCapacity
    val landUnused get() = player.resources.land.unusedAmount
    val foodCapacityLeft get() = player.resources.food.capacityLeft
    val totalFoodCapacity get() = player.buildings.totalFoodCapacity
    //</editor-fold>
    

    fun goHome() {
        screen = HomeScreen()
    }

    override fun toString() = Stringifier.stringify(this)
}
