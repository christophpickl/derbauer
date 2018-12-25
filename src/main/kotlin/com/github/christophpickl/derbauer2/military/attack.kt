package com.github.christophpickl.derbauer2.military

import com.github.christophpickl.derbauer2.VALUES
import com.github.christophpickl.derbauer2.home.HomeView
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


    override fun run() {
        log.debug { "attack thread started" }
        while (!isAttackOver()) {
            nextBattle()
        }
        log.debug { "attack is over" }
        val won = context.enemies == 0
        val additionalText = if (won) {
            val goldEarning = Rand.randomize(context.originalEnemies / 10, 0.5, 1.5)
            Model.gold += goldEarning
            val landEarning = Rand.randomize(context.originalEnemies / 5, 0.3, 2.5)
            Model.land += landEarning
            "\n\n  Gold stolen: $goldEarning\n  Land captured: $landEarning"
        } else {
            ""
        }
        context.message = "You ${if (won) "won" else "lost"}!$additionalText"
        renderer.render()
        sleep(VALUES.attackOverDelay)

        Model.history.attacked++
        Model.currentView = HomeView()
        renderer.render()
    }

    private fun nextBattle() {
        if (playerWonNextBattle()) {
            log.trace { "next battle. enemy lost" }
            context.enemies--
        } else {
            log.trace { "next battle. player lost" }
            Model.player.militaries.killRandom()
        }
        context.message = "Ongoing war ...\n\n" +
            Model.player.militaries.all.joinToString("\n") {
                "  ${it.labelPlural.capitalize()}: ${it.amount}"
            } + "\n\n" +
            "Enemies: ${context.enemies}"

        renderer.render()
        sleep(VALUES.attackBattleDelay)
    }

    private fun playerWonNextBattle(): Boolean {
        var playerRange = 0.5
        val baseSoldier = when (Model.player.militaries.soldiers.amount) {
            in 0..10 -> 0.05
            in 11..25 -> 0.10
            in 25..50 -> 0.15
            else -> 0.20
        }
        playerRange += baseSoldier * Model.player.militaries.soldiers.attackModifier
        return Random.nextDouble(0.0, 1.0) < playerRange
    }

    private fun isAttackOver() = Model.player.militaries.totalCount == 0 || context.enemies == 0

}
