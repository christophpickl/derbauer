package com.github.christophpickl.derbauer.feature

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.misc.OncePredicate
import com.github.christophpickl.kpotpourri.common.misc.OnceResult
import com.github.christophpickl.kpotpourri.common.string.Stringifier

class Features {
    val building = BuildingFeatures()
    val military = MilitaryFeatures()
    val upgrade = UpgradeFeatures()
    val action = ActionFeatures()

    @JsonIgnore val all = building.all.plus(military.all).plus(upgrade.all).plus(action.all)

    override fun toString() = Stringifier.stringify(this)
}

interface Feature {
    fun isEnabled(): Boolean
    fun checkAndNotify()
}

open class AbstractFeature(
    private val notification: String,
    predicate: () -> Boolean
) : Feature {

    private val condition = OncePredicate(predicate)

    override fun isEnabled() = condition.enabled

    override fun checkAndNotify() {
        if (condition.checkAndGet() == OnceResult.ChangedToTrue) {
            Model.notifications.add(notification)
        }
    }

}
