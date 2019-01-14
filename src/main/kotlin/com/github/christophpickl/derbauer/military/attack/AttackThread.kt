package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.amount.summed
import com.github.christophpickl.derbauer.ui.Renderer
import com.github.christophpickl.kpotpourri.common.misc.sleep
import mu.KotlinLogging.logger

class AttackThread(
    private val context: AttackContext,
    private val callback: AttackCallback,
    private val renderer: Renderer
) : Runnable {

    private val log = logger {}
    private val calculator = AttackCalculator(context)

    private val delayInMs = Math.min(500, Math.max(2, (Values.military.attackBattleLastsMs /
        Math.min(context.enemies.real, context.armies.values.summed())).toInt()))

    init {
        log.trace { "attack delay: ${delayInMs}ms" }
    }

    override fun run() {
        log.debug { "attack thread started" }
        while (!context.isAttackOver) {
            nextBattle()
        }
        log.debug { "attack is over" }
        endResult()
    }

    private fun nextBattle() {
        calculator.fightBattle()
        context.message = "You are in battle with: ${context.target.label}\n\n" +
            context.armies.map { "${it.key.labelPlural.capitalize()}: ${it.value.formatted}" }
                .joinToString("\n") +
            "\n\n" +
            "Enemies: ${context.enemies.formatted}"

        renderer.render()
        sleep(delayInMs)
    }

    private fun endResult() {
        callback.onBattleOver(context)
        renderer.render()
    }

}

