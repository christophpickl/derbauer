package com.github.christophpickl.derbauer.army

import com.github.christophpickl.derbauer.logic.ChooseScreen
import com.github.christophpickl.derbauer.logic.EnummedChoice
import com.github.christophpickl.derbauer.logic.NumberInputScreen
import com.github.christophpickl.derbauer.logic.ScreenCallback
import com.github.christophpickl.derbauer.logic.formatNumber
import com.github.christophpickl.derbauer.model.State

enum class ArmyEnum {
    Attack,
    HireSoldiers
}

class ArmyChoice(
    override val enum: Enum<ArmyEnum>,
    override val label: String
) : EnummedChoice<ArmyEnum>


class ArmyScreen() : ChooseScreen<ArmyChoice> {

    private val messages = listOf(
        "Hey, don't look at me dude.",
        "Gonna kick some ass!",
        "Any problem can be solved with brute force and violence."
    )
    override val message = messages.random()

    //@formatter:off
    override val choices = listOf(
        ArmyChoice(ArmyEnum.Attack, "Attack"),
        ArmyChoice(ArmyEnum.HireSoldiers, "Buy soldiers ... ${formatNumber(State.prices.army.soldier, 3)} $ (simple ground unit, attack: ${State.army.soldierAttackStrength})")
    )
    //@formatter:on

    override fun onCallback(callback: ScreenCallback) {
        callback.onArmy(this)
    }

}

class HireSoldiersScreen() : NumberInputScreen {

    override val message = "How many soldiers do you wanna hire?\n\n" +
        "1 costs ${State.prices.army.soldier} gold and 1 person, you can afford ${State.affordableSoldiers} of them."

    override fun onCallback(callback: ScreenCallback) {
        callback.onHireSoldiers(this)
    }

}
