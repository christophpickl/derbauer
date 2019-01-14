package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.math.KMath
import mu.KotlinLogging.logger
import kotlin.random.Random

class AttackCalculator(
    private val context: AttackContext
) {

    private val log = logger {}

    fun fightBattle(): BattleResult {
        var playerRange = 0.5
        val playerUnit = context.armies.filter { it.value.isNotZero }.keys.random()
        playerRange *= playerUnit.attackModifier
        val rand = Random.nextDouble()
        val playerWon = rand < (playerRange + KMath.max(KMath.min((Model.global.karma / 20), -0.1), 0.1))
        log.trace { "rand (${String.format("%.2f", rand)}) < range ($playerRange) => won: $playerWon" }

        return if (playerWon) {
            context.enemies--
            BattleResult.Won
        } else {
            playerUnit.amount--
            context.armies[playerUnit] = context.armies[playerUnit]!! - 1
            log.trace { "Killed: ${playerUnit.label} (amount left: ${context.armies[playerUnit]})" }
            BattleResult.Lost
        }
    }

}

enum class BattleResult {
    Won,
    Lost
}
