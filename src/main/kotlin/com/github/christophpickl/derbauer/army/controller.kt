package com.github.christophpickl.derbauer.army

import com.github.christophpickl.derbauer.logic.ChooseScreenController
import com.github.christophpickl.derbauer.logic.Screen
import com.github.christophpickl.derbauer.logic.beepReturn
import com.github.christophpickl.derbauer.logic.increment
import com.github.christophpickl.derbauer.misc.HomeScreen
import com.github.christophpickl.derbauer.model.State
import com.google.common.eventbus.EventBus
import mu.KotlinLogging
import javax.inject.Inject
import kotlin.reflect.KMutableProperty1

class ArmyController @Inject constructor(
    private val bus: EventBus
) : ChooseScreenController<ArmyChoice, ArmyScreen> {

    private val log = KotlinLogging.logger {}

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
