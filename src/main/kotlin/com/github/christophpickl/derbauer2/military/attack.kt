package com.github.christophpickl.derbauer2.military

import com.github.christophpickl.derbauer2.CHEAT_MODE
import com.github.christophpickl.derbauer2.home.HomeScreen
import com.github.christophpickl.derbauer2.misc.Rand
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.Renderer
import mu.KotlinLogging
import kotlin.random.Random

class AttackContext(
    var enemies: Int
) {
    val originalEnemies = enemies
    var warStarted = false
    var message = ""
}

class AttackThread(
    private val context: AttackContext,
    private val renderer: Renderer
) : Runnable {

    private val log = KotlinLogging.logger {}

    private fun playerWonNextBattle(): Boolean {
        var playerRange = 0.5
        val baseSoldier = when (Model.player.militaries.soldiers.amount) {
            in 0..10 -> 0.05
            in 11..25 -> 0.10
            in 25..50 -> 0.15
            else -> 0.20
        }
        playerRange += baseSoldier * Model.player.militaries.soldiers.attackModifier
        log.trace { "Player attack range: $playerRange" }
        return Random.nextDouble(0.0, 1.0) < playerRange
    }

    override fun run() {
        while (!isAttackOver()) {
            if (playerWonNextBattle()) {
                context.enemies--
            } else {
                Model.player.militaries.killRandom()
            }
            context.message = "Ongoing war ...\n\n" +
                Model.player.militaries.all.joinToString("\n") { "  ${it.labelPlural}" } + "\n\n" +
                "Enemies: ${context.enemies}"

            renderer.render()
            Thread.sleep(if (CHEAT_MODE) 200 else 600)
        }
        val won = context.enemies == 0
        val additionalText = if (won) {
            val goldEarning = Rand.randomize(context.originalEnemies / 20, 0.5, 1.3)
            Model.gold += goldEarning
            val landEarning = Rand.randomize(context.originalEnemies / 10, 0.3, 1.0)
            Model.land += landEarning
            "\n\n  Gold stolen: $goldEarning\n  Land captured: $landEarning"
        } else {
            ""
        }
        context.message = "You ${if (won) "won" else "lost"}!$additionalText"
        renderer.render()
        Thread.sleep(if (CHEAT_MODE) 400 else 2_000)

        Model.history.attacked++
        Model.screen = HomeScreen()
    }

    private fun isAttackOver() = Model.player.militaries.totalCount == 0 || context.enemies == 0

}
