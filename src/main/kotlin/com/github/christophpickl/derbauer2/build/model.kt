package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.misc.Ordered
import com.github.christophpickl.derbauer2.misc.ReflectPlayer
import com.github.christophpickl.derbauer2.misc.ReflectPlayerImpl
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.ordered
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Buyable
import com.github.christophpickl.derbauer2.model.Labeled
import com.github.christophpickl.derbauer2.model.Model
import kotlin.reflect.KMutableProperty1

interface BuildingType : Buyable, Labeled, ReflectPlayer, Ordered {
    var landNeeded: Int
}

object BuildingTypes {
    val house = HouseType()
    val granary = GranaryType()
    val farm = FarmType()
    // MINOR castle

    val all = propertiesOfType<BuildingTypes, BuildingType>(this).ordered()
}

/* FIXME building meta
farmProduction = 2
castlePeopleCapacity = 40
 */
class HouseType : BaseBuildingType<HouseType>(
    labelSingular = "house",
    labelPlural = "houses",
    landNeeded = 1,
    buyPrice = 15,
    playerProperty = PlayerBuildings::houses
) {
    var peopleCapacity = 5
}

class GranaryType : BaseBuildingType<GranaryType>(
    labelSingular = "granary",
    labelPlural = "granaries",
    landNeeded = 1,
    buyPrice = 30,
    playerProperty = PlayerBuildings::granaries
) {
    var foodCapacity = 100
}

class FarmType : BaseBuildingType<FarmType>(
    labelSingular = "farm",
    labelPlural = "farms",
    landNeeded = 2,
    buyPrice = 50,
    playerProperty = PlayerBuildings::farms
)

abstract class BaseBuildingType<T : BuildingType>(
    final override val labelSingular: String,
    final override val labelPlural: String,
    final override var landNeeded: Int,
    final override var buyPrice: Int,
    playerProperty: KMutableProperty1<PlayerBuildings, out PlayerBuilding<T>>,
    reflect: ReflectPlayer = ReflectPlayerImpl(
        host = lazy { Model.player.buildings },
        playerProperty = playerProperty
    )
) : BuildingType, ReflectPlayer by reflect {
    companion object {
        private var counter = 0
    }

    override val order = counter++
    override fun toString() = Stringifier.stringify(this)
}
