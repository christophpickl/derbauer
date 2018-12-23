package com.github.christophpickl.derbauer.model

class Armies {

    var soldiers = 0

    val all = listOf(Armies::soldiers)

    fun allValues() = all.map { it.get(this) }

    val totalCount get() = allValues().sum()

    fun formatAll() = listOf("Soldiers: $soldiers")

    fun reset() {
        soldiers = if (CHEAT_MODE) 10 else 0
    }

    override fun toString() = "Armies{soldiers=$soldiers}"
}
