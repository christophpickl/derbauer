package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Model
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
        calculator.nextBattleWon()
        context.message = "Ongoing war ...\n\n" +
            Model.player.militaries.all.joinToString("\n") {
                "${it.labelPlural.capitalize()}: ${it.amount.formatted}"
            } + "\n\n" +
            "Enemies: ${context.enemies.formatted}"

        renderer.render()
        sleep(Values.militaries.attackBattleDelay)
    }

    private fun endResult() {
        val result = calculator.applyEndResult()
        context.message = FeedbackView.concatMessages(context.message,
            when (result) {
                is AttackResult.Won -> "You won!\n\n" +
                    "Gold stolen: ${result.goldEarning.formatted}\n" +
                    "Land captured: ${result.landEarning.formatted}"
                AttackResult.Lost -> "You lost!"
            }
        )
        renderer.render()
    }

}
