package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.Buildings
import com.github.christophpickl.derbauer.logic.State
import com.github.christophpickl.derbauer.logic.beepReturn
import com.github.christophpickl.derbauer.logic.increment
import mu.KotlinLogging.logger
import javax.inject.Inject
import kotlin.reflect.KMutableProperty1

class BuildScreen(state: State) : ChooseScreen<BuildChoice> {
    override val choices = listOf(
        BuildChoice(BuildEnum.House, "House (costs: ${state.prizes.house})"),
        BuildChoice(BuildEnum.Farm, "Farm (costs: ${state.prizes.farm})")
    )

    override val message = "What do you wanna build?"

    override fun onCallback(callback: ScreenCallback) {
        callback.onBuild(this)
    }

}

enum class BuildEnum {
    House,
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
            BuildEnum.House -> maybeBuild(Buildings::houses, state.prizes.house)
            BuildEnum.Farm -> maybeBuild(Buildings::farms, state.prizes.farm)
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
