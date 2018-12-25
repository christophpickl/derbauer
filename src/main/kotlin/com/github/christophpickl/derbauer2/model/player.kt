package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.build.Buildings
import com.github.christophpickl.derbauer2.military.Militaries
import com.github.christophpickl.derbauer2.upgrade.Upgrades

data class Player(
    val resources: Resources = Resources(),
    val buildings: Buildings = Buildings(),
    val upgrades: Upgrades = Upgrades(),
    val militaries: Militaries = Militaries()
)
