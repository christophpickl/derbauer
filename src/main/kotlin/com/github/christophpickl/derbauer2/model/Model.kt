package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.HomeScreen
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.ui.screen.Screen

object Model {

    lateinit var screen: Screen
    lateinit var global: Global
    lateinit var player: Player
    lateinit var history: History

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

    val availableLand: Int get() = player.resources.land.unusedAmount
    //</editor-fold>

    fun goHome() {
        screen = HomeScreen()
    }

    fun reset() {
        screen = HomeScreen()
        global = Global()
        player = Player()
        history = History()
    }

    override fun toString() = Stringifier.stringify(this)
}
