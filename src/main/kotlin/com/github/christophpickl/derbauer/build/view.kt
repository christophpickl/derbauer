package com.github.christophpickl.derbauer.build

import com.github.christophpickl.derbauer.logic.ChooseScreen
import com.github.christophpickl.derbauer.logic.EnummedChoice
import com.github.christophpickl.derbauer.logic.ScreenCallback
import com.github.christophpickl.derbauer.logic.formatNumber
import com.github.christophpickl.derbauer.model.State

@Deprecated(message = "v2")
enum class BuildEnum {
    House,
    Granary,
    Farm,
    Castle
}

@Deprecated(message = "v2")
class BuildChoice(
    override val enum: Enum<BuildEnum>,
    override val label: String
) : EnummedChoice<BuildEnum>

@Deprecated(message = "v2")
class BuildScreen() : ChooseScreen<BuildChoice> {

    private val messages = listOf(
        "Expand, expand, expand.",
        "Construction work makes me happy."
    )
    override val message = (if (State.freePeople <= 3) "The people desperately need more houses!" else messages.random()) + "\n\n" +
        "You've got the following buildings:\n" +
        State.player.buildings.formatAll().joinToString("\n") {
            "  $it"
        }

    private val colWidth = 3
    override val choices
        get() = mutableListOf(
            //@formatter:off
        BuildChoice(BuildEnum.House,          "House   ... ${formatNumber(State.prices.buildings.house, colWidth)} $ (adds ${State.buildings.houseCapacity} more space for your people)"),
        BuildChoice(BuildEnum.Granary,        "Granary ... ${formatNumber(State.prices.buildings.granary, colWidth)} $ (adds ${State.buildings.granaryCapacity} more food storage)"),
        BuildChoice(BuildEnum.Farm,           "Farm    ... ${formatNumber(State.prices.buildings.farm, colWidth)} $ (produces +${State.buildings.farmProduction} food each day)")
    ).apply { //                              "        ...
        if (State.feature.isCastleEnabled) { //      "        ...
            add(BuildChoice(BuildEnum.Castle, "Castle  ... ${formatNumber(State.prices.buildings.castle, colWidth)} $ (biiig building)"))
        }
    }
    //@formatter:on

    override fun onCallback(callback: ScreenCallback) {
        callback.onBuild(this)
    }

}
