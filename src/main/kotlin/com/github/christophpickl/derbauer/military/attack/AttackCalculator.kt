package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.random.RandomService
import com.github.christophpickl.kpotpourri.common.random.RealRandomService
import mu.KotlinLogging
import kotlin.random.Random

class AttackCalculator(
    private val context: AttackContext,
    private val random: RandomService = RealRandomService
) {

    private val log = KotlinLogging.logger {}

    fun nextBattleWon(): Boolean {
        var playerRange = 0.5
        val playerUnit = Model.player.militaries.all.filter { it.amount > 0 }.random()
        playerRange *= playerUnit.attackModifier
        val rand = Random.nextDouble(0.0, 1.0)
        val playerWon = rand < playerRange
        log.trace { "rand (${String.format("%0.2f", rand)}) < playerRange ($playerRange) => player won: $playerWon" }
        if (playerWon) {
            context.enemies--
        } else {
            playerUnit.amount--
            log.trace { "Killed: ${playerUnit.label} (amount left: ${playerUnit.amount})" }
        }
        return playerWon
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

    fun isAttackOver() = Model.player.militaries.totalAmount.isZero || context.enemies.isZero

}
