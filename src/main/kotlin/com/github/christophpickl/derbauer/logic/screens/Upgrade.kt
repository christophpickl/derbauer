package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.State
import com.github.christophpickl.derbauer.logic.beepReturn
import mu.KotlinLogging.logger
import javax.inject.Inject

class UpgradeScreen(state: State) : ChooseScreen<UpgradeChoice> {
    override val choices = listOf(
        UpgradeChoice(UpgradeEnum.FarmProductivity, "Farm productivity (costs: ${state.prizes.upgrades.farmProductivity})")
    )
    override val message = "What do you wanna upgrade?"
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


class UpgradeController @Inject constructor(
    private val state: State
) : ChooseScreenController<UpgradeChoice, UpgradeScreen> {

    private val log = logger {}

    override fun select(choice: UpgradeChoice) {
        val nextScreen: Screen? = when (choice.enum) {
            UpgradeEnum.FarmProductivity -> maybeUpgrade(state.prizes.upgrades.farmProductivity) {
                state.buildings.farmProduces += 1
                state.prizes.upgrades.farmProductivity += 50
            }
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        nextScreen?.let {
            state.screen = it
        }
    }

    private fun maybeUpgrade(price: Int, successAction: () -> Unit): Screen? {
        return if (state.player.gold < price) {
            log.trace { "Not enough gold (${state.player.gold}), needed: $price" }
            beepReturn<Screen>()
        } else {
            successAction()
            state.player.gold -= price
            HomeScreen(state)
        }
    }

}
