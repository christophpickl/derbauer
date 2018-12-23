package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.beepReturn
import com.github.christophpickl.derbauer.logic.formatNumber
import com.github.christophpickl.derbauer.logic.increment
import com.github.christophpickl.derbauer.model.Buildings
import com.github.christophpickl.derbauer.model.State
import mu.KotlinLogging.logger
import javax.inject.Inject
import kotlin.reflect.KMutableProperty1

class BuildScreen(state: State) : ChooseScreen<BuildChoice> {

    private val messages = listOf(
        "Expand, expand, expand.",
        "Construction work makes me happy."
    )
    override val message = if (state.freePeople <= 3) "The people desperately need more houses!" else messages.random()

    //@formatter:off
    override val choices = listOf(
        BuildChoice(BuildEnum.House,   "House   ... ${formatNumber(state.prices.house, 2)}$"),
        BuildChoice(BuildEnum.Granary, "Granary ... ${formatNumber(state.prices.granary, 2)}$"),
        BuildChoice(BuildEnum.Farm,    "Farm    ... ${formatNumber(state.prices.farm, 2)}$")
    )
    //@formatter:on


    override fun onCallback(callback: ScreenCallback) {
        callback.onBuild(this)
    }

}

enum class BuildEnum {
    House,
    Granary,
    Farm
}

class BuildChoice(
    override val enum: Enum<BuildEnum>,
    override val label: String
) : EnummedChoice<BuildEnum>


class BuildController @Inject constructor(
    private val state: State
) : ChooseScreenController<BuildChoice, BuildScreen> {

    private val log = logger {}
    override fun select(choice: BuildChoice) {
        val nextScreen: Screen? = when (choice.enum) {
            BuildEnum.House -> maybeBuild(Buildings::houses, state.prices.house)
            BuildEnum.Granary -> maybeBuild(Buildings::granaries, state.prices.granary)
            BuildEnum.Farm -> maybeBuild(Buildings::farms, state.prices.farm)
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        nextScreen?.let {
            state.screen = it
        }
    }

    private fun maybeBuild(targetProperty: KMutableProperty1<Buildings, Int>, price: Int): Screen? {
        return if (state.player.gold < price) {
            log.trace { "Not enough gold (${state.player.gold}), needed: $price for ${targetProperty.name}!" }
            beepReturn<Screen>()
        } else if (state.player.land == state.player.buildings.totalCount) {
            log.trace { "Not enough land!" }
            beepReturn<Screen>()
        } else {
            targetProperty.increment(state.player.buildings, 1)
            state.player.gold -= price
            HomeScreen(state)
        }
    }

}
