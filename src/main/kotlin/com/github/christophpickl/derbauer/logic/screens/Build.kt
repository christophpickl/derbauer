package com.github.christophpickl.derbauer.logic.screens

import com.github.christophpickl.derbauer.logic.beepReturn
import com.github.christophpickl.derbauer.logic.formatNumber
import com.github.christophpickl.derbauer.logic.increment
import com.github.christophpickl.derbauer.model.Buildings
import com.github.christophpickl.derbauer.model.State
import mu.KotlinLogging.logger
import kotlin.reflect.KMutableProperty1

class BuildScreen() : ChooseScreen<BuildChoice> {

    private val messages = listOf(
        "Expand, expand, expand.",
        "Construction work makes me happy."
    )
    override val message = if (State.freePeople <= 3) "The people desperately need more houses!" else messages.random() + "\n\n" +
        "You've got the following buildings:\n" +
        State.player.buildings.formatAll().joinToString("\n") {
            "  $it"
        }

    //@formatter:off
    override val choices = listOf(
        BuildChoice(BuildEnum.House,   "House   ... ${formatNumber(State.prices.buildings.house, 2)} $ (adds ${State.buildings.houseCapacity} more space for your people)"),
        BuildChoice(BuildEnum.Granary, "Granary ... ${formatNumber(State.prices.buildings.granary, 2)} $ (adds ${State.buildings.granaryCapacity} more food storage)"),
        BuildChoice(BuildEnum.Farm,    "Farm    ... ${formatNumber(State.prices.buildings.farm, 2)} $ (produces +${State.buildings.farmProduction} food each day)")
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


class BuildController : ChooseScreenController<BuildChoice, BuildScreen> {

    private val log = logger {}
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
