package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.beepReturn
import com.github.christophpickl.derbauer.logic.decrement
import com.github.christophpickl.derbauer.logic.randomize
import com.github.christophpickl.derbauer.model.Armies
import com.github.christophpickl.derbauer.model.State
import com.github.christophpickl.derbauer.view.RenderEvent
import com.google.common.eventbus.EventBus
import mu.KotlinLogging.logger
import javax.inject.Inject
import kotlin.random.Random


class ArmyScreen(state: State) : ChooseScreen<ArmyChoice> {

    override val choices = listOf(
        ArmyChoice(ArmyEnum.Attack, "Attack")
    )

    override val message = "What do you wanna build?"

    override fun onCallback(callback: ScreenCallback) {
        callback.onArmy(this)
    }

}

enum class ArmyEnum {
    Attack
}

class ArmyChoice(
    override val enum: Enum<ArmyEnum>,
    override val label: String
) : EnummedChoice<ArmyEnum>


class ArmyController @Inject constructor(
    private val state: State,
    private val bus: EventBus
) : ChooseScreenController<ArmyChoice, ArmyScreen> {

    private val log = logger {}

    override fun select(choice: ArmyChoice) {
        val nextScreen: Screen? = when (choice.enum) {
            ArmyEnum.Attack -> maybeAttack()
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        nextScreen?.let {
            state.screen = it
        }
    }

    private fun maybeAttack(): Screen? {
        return if (state.player.armies.totalCount == 0) {
            log.trace { "No army available!" }
            beepReturn<Screen>()
        } else {
            AttackOngoingScreen(state, bus)
        }
    }

}

class AttackOngoingScreen(
    private val state: State,
    private val bus: EventBus
) : Screen {

    override val promptEnabled = false
    override val enableCancelOnEnter: Boolean = false
    val context = AttackContext(
        enemies = (Random.nextDouble(0.4, 1.1) * state.player.armies.totalCount).toInt(),
        message = ""
    )

    override val message: String get() = context.message

    override fun onCallback(callback: ScreenCallback) {
        if (!context.warStarted) {
            context.warStarted = true
            Thread(AttackThread(state, context, bus)).start()
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
    private val state: State,
    private val context: AttackContext,
    private val bus: EventBus
) : Runnable {

    private val log = logger {}

    override fun run() {
        while (!isAttackOver()) {
            val playerLost = Random.nextBoolean()
            if (playerLost) {
                state.player.armies.killRandom()
            } else {
                context.enemies--
            }
            context.message = """
                Ongoing war ...
                
                ${state.player.armies.formatAll().joinToString("\n                ") { "  $it" }}
                
                  Enemies: ${context.enemies}
                """.trimIndent()
            bus.post(RenderEvent)
            Thread.sleep(600)
        }
        val won = context.enemies == 0
        val additionalText = if (won) {
            val goldEarning = randomize(context.originalEnemies / 20, 0.5, 1.3)
            state.player.gold += goldEarning
            val landEarning = randomize(context.originalEnemies / 10, 0.3, 1.0)
            state.player.land += landEarning
            "\n\n  Gold stolen: $goldEarning\n  Land captured: $landEarning"
        } else {
            ""
        }
        context.message = "You ${if (won) "won" else "lost"}!$additionalText"
        bus.post(RenderEvent)
        Thread.sleep(2_000)

        state.screen = HomeScreen(state)
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

    private fun isAttackOver() = state.player.armies.totalCount == 0 || context.enemies == 0

}
