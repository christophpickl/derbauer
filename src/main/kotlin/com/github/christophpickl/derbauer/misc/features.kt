package com.github.christophpickl.derbauer.misc

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.CHEAT_MODE
import com.github.christophpickl.derbauer.action.ActionFeatures
import com.github.christophpickl.derbauer.building.BuildingFeatures
import com.github.christophpickl.derbauer.military.MilitaryFeatures
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.trade.TradeFeatures
import com.github.christophpickl.derbauer.upgrade.UpgradeFeatures
import com.github.christophpickl.kpotpourri.common.misc.OncePredicate
import com.github.christophpickl.kpotpourri.common.misc.OnceResult
import com.github.christophpickl.kpotpourri.common.string.Stringifier

class Features {
    val trade = TradeFeatures()
    val building = BuildingFeatures()
    val military = MilitaryFeatures()
    val upgrade = UpgradeFeatures()
    val action = ActionFeatures()

    @JsonIgnore val all: List<Feature> =
        trade.all
            .plus(building.all)
            .plus(military.all)
            .plus(upgrade.all)
            .plus(action.all)

    fun checkAndNotifyAll() {
        all.forEach {
            it.checkAndNotify()
        }
    }

    override fun toString() = Stringifier.stringify(this)
}

interface Feature {
    fun isEnabled(): Boolean
    fun checkAndNotify()
    fun enableCheat()
}

open class AbstractFeature(
    private val notification: String,
    predicate: () -> Boolean
) : Feature {

    private val condition = OncePredicate(predicate)

    private var isCheatEenabled = false

    override fun enableCheat() {
        isCheatEenabled = true
    }

    override fun isEnabled() = condition.enabled || (CHEAT_MODE && isCheatEenabled)

    override fun checkAndNotify() {
        if (condition.checkAndGet() == OnceResult.ChangedToTrue) {
            Model.notifications.add(notification)
        }
    }

}
