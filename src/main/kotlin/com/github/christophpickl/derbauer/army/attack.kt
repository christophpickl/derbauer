package com.github.christophpickl.derbauer.army

import com.github.christophpickl.derbauer.logic.Screen
import com.github.christophpickl.derbauer.logic.ScreenCallback
import com.github.christophpickl.derbauer.logic.decrement
import com.github.christophpickl.derbauer.logic.randomize
import com.github.christophpickl.derbauer.misc.HomeScreen
import com.github.christophpickl.derbauer.model.DEPRECATED_CHEAT_MODE
import com.github.christophpickl.derbauer.model.State
import com.github.christophpickl.derbauer.view.RenderEvent
import com.google.common.eventbus.EventBus
import mu.KotlinLogging.logger
import kotlin.random.Random

class AttackOngoingScreen(
    private val bus: EventBus
) : Screen {

    override val promptEnabled = false
    override val enableCancelOnEnter: Boolean = false
    val context = AttackContext(
        enemies = (Random.nextDouble(0.4, 1.1) * State.player.armies.totalCount).toInt(),
        message = ""
    )

    override val message: String get() = context.message

    override fun onCallback(callback: ScreenCallback) {
        if (!context.warStarted) {
            context.warStarted = true
            Thread(AttackThread(context, bus)).start()
        }
    }

}

class AttackContext(
    var enemies: Int,
    var message: String
) {
    val originalEnemies = enemies
    var warStarted = false
}

private class AttackThread(
    private val context: AttackContext,
    private val bus: EventBus
) : Runnable {

    private val log = logger {}

    private fun playerWonNextBattle(): Boolean {
        var playerRange = 0.5
        val baseSoldier = when (State.player.armies.soldiers) {
            in 0..10 -> 0.05
            in 11..25 -> 0.10
            in 25..50 -> 0.15
            else -> 0.20
        }
        playerRange += baseSoldier * State.army.soldierAttackStrength
        log.trace { "Player attack range: $playerRange" }
        return Random.nextDouble(0.0, 1.0) < playerRange
    }

    override fun run() {
        while (!isAttackOver()) {
            if (playerWonNextBattle()) {
                context.enemies--
            } else {
                State.player.armies.killRandom()
            }
            context.message = """
                Ongoing war ...
                
                ${State.player.armies.formatAll().joinToString("\n                ") { "  $it" }}
                
                  Enemies: ${context.enemies}
                """.trimIndent()
            bus.post(RenderEvent)
            Thread.sleep(if (DEPRECATED_CHEAT_MODE) 200 else 600)
        }
        val won = context.enemies == 0
        val additionalText = if (won) {
            val goldEarning = randomize(context.originalEnemies / 20, 0.5, 1.3)
            State.player.gold += goldEarning
            val landEarning = randomize(context.originalEnemies / 10, 0.3, 1.0)
            State.player.land += landEarning
            "\n\n  Gold stolen: $goldEarning\n  Land captured: $landEarning"
        } else {
            ""
        }
        context.message = "You ${if (won) "won" else "lost"}!$additionalText"
        bus.post(RenderEvent)
        Thread.sleep(if (DEPRECATED_CHEAT_MODE) 400 else 2_000)

        State.history.attacked++
        State.screen = HomeScreen()
        bus.post(RenderEvent)
    }

    private fun Armies.killRandom() {
        require(totalCount > 0)
        val armyType = all.first {
            it.get(this) != 0
        }
        armyType.decrement(this, 1)
        log.trace { "Army unit killed of type: ${armyType.name}" }
    }

    private fun isAttackOver() = State.player.armies.totalCount == 0 || context.enemies == 0

}
