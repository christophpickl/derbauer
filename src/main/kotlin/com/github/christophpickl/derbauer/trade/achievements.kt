package com.github.christophpickl.derbauer.trade

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.endturn.achievement.AbstractAchievement
import com.github.christophpickl.derbauer.endturn.achievement.Achievement
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType

@Suppress("unused")
class TradeAchievements {
    val trade1 = Trade1Achievement()

    @get:JsonIgnore val all get() = propertiesOfType<TradeAchievements, Achievement>(this)
}

class Trade1Achievement : AbstractAchievement(
    label = "Trade Mastery I: Better rates at market place and +${Values.achievements.trade1GoldReward.formatted} gold"
) {
    override fun condition() = Model.history.traded >= Values.achievements.trade1HistoryNeed

    override fun execute() {
        Model.gold += Values.achievements.trade1GoldReward
        Model.player.resources.allTradeables.forEach {
            it.buyPriceModifier -= Values.achievements.trade1PriceModifier
            it.sellPriceModifier -= Values.achievements.trade1PriceModifier
        }
    }
}
