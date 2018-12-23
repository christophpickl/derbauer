package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.beepReturn
import com.github.christophpickl.derbauer.logic.formatNumber
import com.github.christophpickl.derbauer.model.State
import mu.KotlinLogging.logger

class UpgradeScreen() : ChooseScreen<UpgradeChoice> {

    private val messages = listOf(
        "Bigger, stronger, faster! That's me.",
        "Always keep your stuff up2date.",
        "You want to play with the big boys, you first need big equipment."
    )
    override val message = messages.random()

    override val choices = listOf(
        UpgradeChoice(UpgradeEnum.FarmProductivity, "Farm productivity ... ${formatNumber(State.prices.upgrades.farmProductivity, 3)} $")
    )
    override fun onCallback(callback: ScreenCallback) {
        callback.onUpgrade(this)
    }
}

enum class UpgradeEnum {
    FarmProductivity
}

class UpgradeChoice(
    override val enum: Enum<UpgradeEnum>,
    override val label: String
) : EnummedChoice<UpgradeEnum>


class UpgradeController : ChooseScreenController<UpgradeChoice, UpgradeScreen> {

    private val log = logger {}

    override fun select(choice: UpgradeChoice) {
        val nextScreen: Screen? = when (choice.enum) {
            UpgradeEnum.FarmProductivity -> maybeUpgrade(State.prices.upgrades.farmProductivity) {
                State.buildings.farmProduces += 1
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
