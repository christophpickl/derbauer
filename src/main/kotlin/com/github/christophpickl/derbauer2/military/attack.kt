package com.github.christophpickl.derbauer2.military

import com.github.christophpickl.derbauer2.VALUES
import com.github.christophpickl.derbauer2.misc.Rand
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.sleep
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.Renderer
import mu.KotlinLogging.logger
import kotlin.random.Random

class AttackContext(
    var enemies: Int
) {
    val originalEnemies = enemies
    var message = ""

    override fun toString() = Stringifier.stringify(this)
}

class AttackThread(
    private val context: AttackContext,
    private val renderer: Renderer
) : Runnable {

    private val log = logger {}
    private val calculator = AttackCalculator(context)

    override fun run() {
        log.debug { "attack thread started" }
        while (!calculator.isAttackOver()) {
            nextBattle()
        }
        log.debug { "attack is over" }
        endResult()
        Model.goHome()
        renderer.render()
    }

    private fun nextBattle() {
        calculator.nextBattleWon()
        context.message = "Ongoing war ...\n\n" +
            Model.player.militaries.all.joinToString("\n") {
                "${it.labelPlural.capitalize()}: ${it.amount}"
            } + "\n\n" +
            "Enemies: ${context.enemies}"

        renderer.render()
        sleep(VALUES.attackBattleDelay)
    }

    private fun endResult() {
        val result = calculator.applyEndResult()
        context.message = when (result) {
            is AttackResult.Won -> "You won!\n\n" +
                "Gold stolen: ${result.goldEarning}\n" +
                "Land captured: ${result.landEarning}"
            AttackResult.Lost -> "You lost!"
        }
        renderer.render()
        sleep(VALUES.attackOverDelay)
    }

}

sealed class AttackResult {
    class Won(
        val goldEarning: Int,
        val landEarning: Int
    ) : AttackResult()

    object Lost : AttackResult()
}

class AttackCalculator(
    private val context: AttackContext
) {

    private val log = logger {}

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
        val won = context.enemies == 0
        Model.history.attacked++
        return if (won) {
            val goldEarning = Rand.randomize(context.originalEnemies / 10, 0.5, 1.5)
            val landEarning = Rand.randomize(context.originalEnemies / 5, 0.3, 2.5)
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

    fun isAttackOver() = Model.player.militaries.totalAmount == 0 || context.enemies == 0

}
