package com.github.christophpickl.derbauer.build

import com.github.christophpickl.derbauer.logic.ChooseScreen
import com.github.christophpickl.derbauer.logic.EnummedChoice
import com.github.christophpickl.derbauer.logic.ScreenCallback
import com.github.christophpickl.derbauer.logic.formatNumber
import com.github.christophpickl.derbauer.model.State

enum class BuildEnum {
    House,
    Granary,
    Farm
}

class BuildChoice(
    override val enum: Enum<BuildEnum>,
    override val label: String
) : EnummedChoice<BuildEnum>

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
