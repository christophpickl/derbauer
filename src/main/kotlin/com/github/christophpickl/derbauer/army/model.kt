package com.github.christophpickl.derbauer.army

import com.github.christophpickl.derbauer.model.DEPRECATED_CHEAT_MODE
import com.github.christophpickl.derbauer.model.State

val State.affordableSoldiers get() = Math.min(player.gold / prices.army.soldier, player.people)

class Armies {

    var soldiers = 0

    val all = listOf(Armies::soldiers)

    fun allValues() = all.map { it.get(this) }

    val totalCount get() = allValues().sum()

    fun formatAll() = listOf("Soldiers: $soldiers")

    fun reset() {
        soldiers = if (DEPRECATED_CHEAT_MODE) 10 else 0
    }

    override fun toString() = "Armies{soldiers=$soldiers}"
}

class ArmyPrices {
    var soldier = 0

    fun reset() {
        soldier = 20
    }

    override fun toString() = "ArmyPrices{soldier=$soldier}"
}

class ArmyMeta {
    var soldierAttackStrength = 0.0

    fun reset() {
        soldierAttackStrength = 1.0
    }

    override fun toString() = "ArmyMeta{soldierAttackStrength=$soldierAttackStrength}"

}
