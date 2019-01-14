package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.derbauer.military.Army
import com.github.christophpickl.derbauer.military.attack.target.AttackTarget
import com.github.christophpickl.derbauer.military.attack.target.Targets
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.kpotpourri.common.string.Stringifier
import java.util.*

data class Military(
    val targets: Targets = Targets()
)

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
    val target: AttackTarget,
    var enemies: Amount
) {
    var message = ""
    
    val armies: MutableMap<Army, Amount> = IdentityHashMap(armies)
    val originalEnemies = enemies
    val isAttackOver get() = allArmiesDead || allEnemiesDead
    val isBattleWon get() = allEnemiesDead

    private val allArmiesDead get() = armies.all { it.value.isZero }
    private val allEnemiesDead get() = enemies.isZero

    override fun toString() = Stringifier.stringify(this)
}
