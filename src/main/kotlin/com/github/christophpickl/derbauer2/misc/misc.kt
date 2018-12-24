package com.github.christophpickl.derbauer2.misc


interface Listener {}

class Subscription<L : Listener> {

    private val listeners = mutableListOf<L>()

    fun add(listener: L) {
        listeners += listener
    }

    fun broadcast(action: L.() -> Unit) {
        listeners.forEach {
            action(it)
        }
    }
}

@Suppress("unused")
fun Any?.enforceWhenBranches() {
} 
