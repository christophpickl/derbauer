package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.derbauer.military.Army
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.kpotpourri.common.string.Stringifier
import java.util.*

data class PrepareAttackContext(
    val armies: LinkedHashMap<Army, Amount?>
) {
    private val iterator = armies.iterator()
    private var _current = iterator.next()
    val current get() = _current

    fun setCurrentAmount(amount: Amount) {
        armies[current.key] = amount
    }

    fun next() {
        _current = iterator.next()
    }

    fun isDone() = !iterator.hasNext()

}

class AttackContext(
    armies: Map<Army, Amount>,
    var enemies: Amount
) {
    val armies: MutableMap<Army, Amount> = IdentityHashMap(armies)
    val originalEnemies = enemies
    var message = ""
    var attackOver = false

    val allArmiesDead get() = armies.all { it.value.isZero }
    val allEnemiesDead get() = enemies.isZero

    override fun toString() = Stringifier.stringify(this)
}

sealed class AttackResult {
    class Won(
        val goldEarning: Amount,
        val landEarning: Amount
    ) : AttackResult()

    object Lost : AttackResult()
}
