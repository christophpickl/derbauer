package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.kpotpourri.common.math.KMath
import com.github.christophpickl.kpotpourri.common.random.RandomService
import com.github.christophpickl.kpotpourri.common.random.RealRandomService
import mu.KotlinLogging.logger
import kotlin.random.Random

class AttackCalculator(
    private val context: AttackContext,
    private val random: RandomService = RealRandomService
) {

    private val log = logger {}

    fun fightBattle(): BattleResult {
        var playerRange = 0.5
        val playerUnit = context.armies.filter { it.value.isNotZero }.keys.random()
        playerRange *= playerUnit.attackModifier
        val rand = Random.nextDouble()
        val playerWon = rand < (playerRange + KMath.max(KMath.min((Model.global.karma / 20), -0.1), 0.1))
        log.trace { "rand (${String.format("%.2f", rand)}) < playerRange ($playerRange) => player won: $playerWon" }
        if (playerWon) {
            context.enemies--
        } else {
            playerUnit.amount--
            context.armies[playerUnit] = context.armies[playerUnit]!! - 1
            log.trace { "Killed: ${playerUnit.label} (amount left: ${context.armies[playerUnit]})" }
        }
        return if (playerWon) BattleResult.Won else BattleResult.Lost
    }

    fun applyEndResult(): AttackResult {
        val won = context.enemies.isZero
        context.attackOver = true
        Model.history.attacked++
        return if (won) {
            val goldEarning = Amount(random.randomize((context.originalEnemies / 10).real, 0.5, 1.5))
            val landEarning = Amount(random.randomize((context.originalEnemies / 5).real, 0.3, 2.5))
            Model.gold += goldEarning
            Model.land += landEarning
            AttackResult.Won(
                goldEarning = goldEarning,
                landEarning = landEarning
            )
        } else {
            AttackResult.Lost
        }
    }

    fun isAttackOver() = context.allArmiesDead || context.allEnemiesDead

}

enum class BattleResult {
    Won,
    Lost
}
