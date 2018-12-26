package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.kpotpourri.common.string.Stringifier

class AttackContext(
    var enemies: Int
) {
    val originalEnemies = enemies
    var message = ""
    var attackOver = false

    override fun toString() = Stringifier.stringify(this)
}

sealed class AttackResult {
    class Won(
        val goldEarning: Int,
        val landEarning: Int
    ) : AttackResult()

    object Lost : AttackResult()
}
