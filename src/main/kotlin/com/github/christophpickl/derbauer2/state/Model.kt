package com.github.christophpickl.derbauer2.state

import com.github.christophpickl.derbauer.model.History
import com.github.christophpickl.derbauer2.Screen
import com.github.christophpickl.derbauer2.domain.HomeScreen
import com.github.christophpickl.derbauer2.misc.Stringifier

object Model {

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

    override fun toString() = Stringifier.stringify(this)
}
