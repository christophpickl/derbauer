package com.github.christophpickl.derbauer.achievement

import com.github.christophpickl.derbauer.model.DEPRECATED_CHEAT_MODE
import com.github.christophpickl.derbauer.model.State

class Trade1Achievement() : Achievement(
    message = "Trade Mastery I: Cheaper trade rates"
) {
    private val tradeThreshold = if (DEPRECATED_CHEAT_MODE) 1 else 10

    override fun condition() =
        State.history.traded >= tradeThreshold

    override fun changeState() {
        State.prices.trade.decreaseAllBy(0.2)
    }

}

class Army1Achievement() : Achievement(
    message = "Military Mastery I: Soldier attack +30%"
) {
    private val attackThreshold = if (DEPRECATED_CHEAT_MODE) 1 else 10

    override fun condition() =
        State.history.attacked >= attackThreshold

    override fun changeState() {
        State.army.soldierAttackStrength += 0.3
    }

}
