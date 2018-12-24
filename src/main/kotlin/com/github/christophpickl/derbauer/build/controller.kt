package com.github.christophpickl.derbauer.build

import com.github.christophpickl.derbauer.logic.ChooseScreenController
import com.github.christophpickl.derbauer.logic.Screen
import com.github.christophpickl.derbauer.logic.beepReturn
import com.github.christophpickl.derbauer.logic.increment
import com.github.christophpickl.derbauer.misc.HomeScreen
import com.github.christophpickl.derbauer.model.State
import mu.KotlinLogging
import kotlin.reflect.KMutableProperty1

@Deprecated(message = "v2")
class BuildController : ChooseScreenController<BuildChoice, BuildScreen> {

    private val log = KotlinLogging.logger {}
    override fun select(choice: BuildChoice) {
        val nextScreen: Screen? = when (choice.enum) {
            BuildEnum.House -> maybeBuild(Buildings::houses, State.prices.buildings.house)
            BuildEnum.Granary -> maybeBuild(Buildings::granaries, State.prices.buildings.granary)
            BuildEnum.Farm -> maybeBuild(Buildings::farms, State.prices.buildings.farm)
            else -> throw UnsupportedOperationException("Unhandled choice enum: ${choice.enum}")
        }
        nextScreen?.let {
            State.screen = it
        }
    }

    private fun maybeBuild(targetProperty: KMutableProperty1<Buildings, Int>, price: Int): Screen? {
        return if (State.player.gold < price) {
            log.trace { "Not enough gold (${State.player.gold}), needed: $price for ${targetProperty.name}!" }
            beepReturn<Screen>()
        } else if (State.player.land == State.player.buildings.totalCount) {
            log.trace { "Not enough land!" }
            beepReturn<Screen>()
        } else {
            targetProperty.increment(State.player.buildings, 1)
            State.player.gold -= price
            HomeScreen()
        }
    }

}
