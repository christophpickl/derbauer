package com.github.christophpickl.derbauer.upgrade

import com.github.christophpickl.derbauer.logic.ChooseScreen
import com.github.christophpickl.derbauer.logic.EnummedChoice
import com.github.christophpickl.derbauer.logic.ScreenCallback
import com.github.christophpickl.derbauer.logic.formatNumber
import com.github.christophpickl.derbauer.model.State

enum class UpgradeEnum {
    FarmProductivity
}

class UpgradeChoice(
    override val enum: Enum<UpgradeEnum>,
    override val label: String
) : EnummedChoice<UpgradeEnum>


class UpgradeScreen() : ChooseScreen<UpgradeChoice> {

    private val messages = listOf(
        "Bigger, stronger, faster! That's me.",
        "Always keep your stuff up2date.",
        "You want to play with the big boys, you first need big equipment."
    )
    override val message = messages.random()

    override val choices = listOf(
        UpgradeChoice(UpgradeEnum.FarmProductivity, "Farm productivity ... " +
            "${formatNumber(State.prices.upgrades.farmProductivity, 3)} $ (increases food output by +${State.upgrades.increaseFarmProduction})")
    )
    override fun onCallback(callback: ScreenCallback) {
        callback.onUpgrade(this)
    }
}
