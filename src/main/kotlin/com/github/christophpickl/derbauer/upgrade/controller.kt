package com.github.christophpickl.derbauer.upgrade

import com.github.christophpickl.derbauer.logic.ChooseScreenController
import com.github.christophpickl.derbauer.logic.Screen
import com.github.christophpickl.derbauer.logic.beepReturn
import com.github.christophpickl.derbauer.misc.HomeScreen
import com.github.christophpickl.derbauer.model.State
import mu.KotlinLogging

@Deprecated(message = "v2")
class UpgradeController : ChooseScreenController<UpgradeChoice, UpgradeScreen> {

    private val log = KotlinLogging.logger {}

    override fun select(choice: UpgradeChoice) {
        val nextScreen: Screen? = when (choice.enum) {
            UpgradeEnum.FarmProductivity -> maybeUpgrade(State.prices.upgrades.farmProductivity) {
                State.buildings.farmProduction += State.upgrades.increaseFarmProduction
                State.prices.upgrades.farmProductivity *= 2
            }
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        nextScreen?.let {
            State.screen = it
        }
    }

    private fun maybeUpgrade(price: Int, successAction: () -> Unit): Screen? {
        return if (State.player.gold < price) {
            log.trace { "Not enough gold (${State.player.gold}), needed: $price" }
            beepReturn<Screen>()
        } else {
            successAction()
            State.player.gold -= price
            HomeScreen()
        }
    }

}
