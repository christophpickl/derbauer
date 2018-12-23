package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.beepReturn
import com.github.christophpickl.derbauer.logic.decrement
import com.github.christophpickl.derbauer.logic.formatNumber
import com.github.christophpickl.derbauer.logic.increment
import com.github.christophpickl.derbauer.logic.randomize
import com.github.christophpickl.derbauer.model.Armies
import com.github.christophpickl.derbauer.model.CHEAT_MODE
import com.github.christophpickl.derbauer.model.State
import com.github.christophpickl.derbauer.view.RenderEvent
import com.google.common.eventbus.EventBus
import mu.KotlinLogging.logger
import javax.inject.Inject
import kotlin.random.Random
import kotlin.reflect.KMutableProperty1


class ArmyScreen() : ChooseScreen<ArmyChoice> {

    private val messages = listOf(
        "Hey, don't look at me dude.",
        "Gonna kick some ass!",
        "Any problem can be solved with brute force and violence."
    )
    override val message = messages.random()

    //@formatter:off
    override val choices = listOf(
        ArmyChoice(ArmyEnum.Attack, "Attack"),
        ArmyChoice(ArmyEnum.HireSoldiers, "Buy soldiers ... ${formatNumber(State.prices.army.soldier, 3)} $")
    )
    //@formatter:on

    override fun onCallback(callback: ScreenCallback) {
        callback.onArmy(this)
    }

}

enum class ArmyEnum {
    Attack,
    HireSoldiers
}

class ArmyChoice(
    override val enum: Enum<ArmyEnum>,
    override val label: String
) : EnummedChoice<ArmyEnum>


class ArmyController @Inject constructor(
    private val bus: EventBus
) : ChooseScreenController<ArmyChoice, ArmyScreen> {

    private val log = logger {}

    override fun select(choice: ArmyChoice) {
        val nextScreen: Screen? = when (choice.enum) {
            ArmyEnum.Attack -> maybeAttack()
            ArmyEnum.HireSoldiers -> HireSoldiersScreen()
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        nextScreen?.let {
            State.screen = it
        }
    }

    private fun maybeAttack(): Screen? {
        return if (State.player.armies.totalCount == 0) {
            log.trace { "No army available!" }
            beepReturn<Screen>()
        } else {
            AttackOngoingScreen(bus)
        }
    }

    fun hireSoldier(amount: Int) {
        maybeHire(Armies::soldiers, amount, State.prices.army.soldier)?.let {
            State.screen = it
        }
    }

    private fun maybeHire(targetProperty: KMutableProperty1<Armies, Int>, amount: Int, pricePerUnit: Int): Screen? {
        val price = amount * pricePerUnit
        return if (State.player.gold < price) {
            log.trace { "Not enough gold (${State.player.gold}), needed: $price for $amount ${targetProperty.name}(s)." }
            beepReturn<Screen>()
        } else if (State.player.people < amount) {
            log.trace { "Not enough people!" }
            beepReturn<Screen>()
        } else {
            targetProperty.increment(State.player.armies, amount)
            State.player.gold -= price
            State.player.people -= amount
            HomeScreen()
        }
    }
}

class HireSoldiersScreen() : NumberInputScreen {

    override val message = "How many soldiers do you wanna hire?\n\n" +
        "1 costs ${State.prices.army.soldier} gold and 1 person, you can afford ${State.affordableSoldiers} of them."

    override fun onCallback(callback: ScreenCallback) {
        callback.onHireSoldiers(this)
    }

}

val State.affordableSoldiers get() = Math.min(player.gold / prices.army.soldier, player.people)

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
            Thread.sleep(if (CHEAT_MODE) 200 else 600)
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
        Thread.sleep(if (CHEAT_MODE) 400 else 2_000)

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
