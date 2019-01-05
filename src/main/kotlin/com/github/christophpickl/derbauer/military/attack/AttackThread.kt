package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.ui.Renderer
import com.github.christophpickl.derbauer.ui.view.FeedbackView
import com.github.christophpickl.kpotpourri.common.misc.sleep
import mu.KotlinLogging

class AttackThread(
    private val context: AttackContext,
    private val renderer: Renderer
) : Runnable {

    private val log = KotlinLogging.logger {}
    private val calculator = AttackCalculator(context)

    override fun run() {
        log.debug { "attack thread started" }
        while (!calculator.isAttackOver()) {
            nextBattle()
        }
        log.debug { "attack is over" }
        endResult()
    }

    private fun nextBattle() {
        calculator.fightBattle()
        context.message = "Ongoing war ...\n\n" +
            context.armies.map { "${it.key.labelPlural.capitalize()}: ${it.value.formatted}" }.joinToString("\n") + "\n\n" +
            "Enemies: ${context.enemies.formatted}"

        renderer.render()
        sleep(Values.militaries.attackBattleDelay)
    }

    private fun endResult() {
        val result = calculator.applyEndResult()
        context.message = FeedbackView.concatMessages(context.message,
            when (result) {
                is AttackResult.Won -> "You won! This is your loot:\n\n" +
                    "Gold stolen: ${result.goldEarning.formatted}\n" +
                    "Land captured: ${result.landEarning.formatted}"
                AttackResult.Lost -> "You lost! No loot for you, poor bastard."
            }
        )
        renderer.render()
    }

}
