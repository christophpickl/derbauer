package com.github.christophpickl.derbauer.model

import com.github.christophpickl.derbauer.build.Buildings
import com.github.christophpickl.derbauer.military.Militaries
import com.github.christophpickl.derbauer.upgrade.Upgrades

data class Player(
    val resources: Resources = Resources(),
    val buildings: Buildings = Buildings(),
    val upgrades: Upgrades = Upgrades(),
    val militaries: Militaries = Militaries()
)
