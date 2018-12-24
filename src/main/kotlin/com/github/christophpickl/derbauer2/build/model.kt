package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.misc.ReflectPlayer
import com.github.christophpickl.derbauer2.misc.ReflectPlayerImpl
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Buyable
import com.github.christophpickl.derbauer2.model.Labeled
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.PlayerBuilding
import com.github.christophpickl.derbauer2.model.PlayerBuildings

interface BuildingType : Buyable, Labeled, ReflectPlayer {
    var landNeeded: Int
}

object BuildingTypes {
    val house = HouseType()

    val all = propertiesOfType<BuildingTypes, BuildingType>(this)
}

class HouseType(
    reflect: ReflectPlayerImpl<PlayerBuildings, PlayerBuilding> = ReflectPlayerImpl(
        host = Model.player.buildings,
        playerProperty = PlayerBuildings::houses
    )
) : BuildingType, ReflectPlayer by reflect {
    override val labelSingular = "house"
    override val labelPlural = "houses"
    override var landNeeded = 1
    override var buyPrice = 15
}
