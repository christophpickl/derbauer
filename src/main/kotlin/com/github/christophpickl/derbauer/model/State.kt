@file:Suppress("ConstantConditionIf")

package com.github.christophpickl.derbauer.model

import com.github.christophpickl.derbauer.army.ArmyMeta
import com.github.christophpickl.derbauer.build.BuildingsMeta
import com.github.christophpickl.derbauer.logic.Prompt
import com.github.christophpickl.derbauer.logic.Screen
import com.github.christophpickl.derbauer.misc.Feature
import com.github.christophpickl.derbauer.upgrade.UpgradeMeta
import com.github.christophpickl.derbauer2.misc.Stringifier

@Deprecated(message = "v2")
const val DEPRECATED_CHEAT_MODE = true

@Deprecated(message = "v2")
object State {

    lateinit var screen: Screen

    var day = 0
    val prompt = Prompt()
    val player = Player()
    val prices = Prices()
    val buildings = BuildingsMeta()
    val army = ArmyMeta()
    val meta = StateMeta()
    val history = History()
    val upgrades = UpgradeMeta()
    val feature = Feature()

    val maxFood get() = player.buildings.granaries * buildings.granaryCapacity
    val freeFood get() = maxFood - player.food
    val maxPeople get() = player.buildings.houses * buildings.houseCapacity
    val freePeople get() = maxPeople - player.people

    init {
        reset()
    }

    fun reset() {
        day = 1
        player.reset()
        prices.reset()
        buildings.reset()
        army.reset()
        meta.reset()
        history.reset()
        upgrades.reset()
        feature.reset()
    }

    override fun toString() = Stringifier.stringify(this)
}

class StateMeta {
    var reproductionRate = 0.0

    fun reset() {
        reproductionRate = 0.1
    }

    override fun toString() = Stringifier.stringify(this)

}

@Deprecated(message = "v2")
class History {

    var traded = 0
    var attacked = 0

    fun reset() {
        traded = 0
        attacked = 0
    }

    override fun toString() = Stringifier.stringify(this)
}
