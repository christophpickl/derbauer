package com.github.christophpickl.derbauer2.state

import com.github.christophpickl.derbauer.model.History
import com.github.christophpickl.derbauer2.domain.HomeScreen
import com.github.christophpickl.derbauer2.misc.Screen

object State {

    lateinit var screen: Screen
    lateinit var global: Global
    lateinit var player: Player
    lateinit var history: History

    var gold
        get() = player.resources.gold.amount
        set(value) {
            player.resources.gold.amount = value
        }

    fun goHome() {
        screen = HomeScreen()
    }

    fun reset() {
        screen = HomeScreen()
        global = Global()
        player = Player()
        history = History()
    }
}
