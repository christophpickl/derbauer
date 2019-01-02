package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.kpotpourri.common.string.Stringifier

class AttackContext(
    var enemies: Amount
) {
    val originalEnemies = enemies
    var message = ""
    var attackOver = false

    override fun toString() = Stringifier.stringify(this)
}

sealed class AttackResult {
    class Won(
        val goldEarning: Amount,
        val landEarning: Amount
    ) : AttackResult()

    object Lost : AttackResult()
}
