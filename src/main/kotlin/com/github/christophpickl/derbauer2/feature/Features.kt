package com.github.christophpickl.derbauer2.feature

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.misc.OncePredicate
import com.github.christophpickl.derbauer2.misc.OnceResult
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.model.Model

class Features {
    val building = BuildingFeatures()
    val military = MilitaryFeatures()
    val upgrade = UpgradeFeatures()

    @JsonIgnore val all = building.all.plus(military.all).plus(upgrade.all)

    override fun toString() = Stringifier.stringify(this)
}

interface Feature {
    fun isEnabled(): Boolean
}

open class AbstractFeature(
    private val notification: String,
    predicate: () -> Boolean
) : Feature {

    private val condition = OncePredicate(predicate)

    override fun isEnabled() =
        when (condition.checkAndGet()) {
            OnceResult.Unfilfilled -> false
            OnceResult.ChangedToTrue -> {
                Model.notifications.add(notification); true
            }
            OnceResult.WasAlready -> true
        }

}
