package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.build.PlayerBuildings
import com.github.christophpickl.derbauer2.military.PlayerMilitaries
import com.github.christophpickl.derbauer2.upgrade.PlayerUpgrades

data class Player(
    val resources: PlayerResources = PlayerResources(),
    val buildings: PlayerBuildings = PlayerBuildings(),
    val upgrades: PlayerUpgrades = PlayerUpgrades(),
    val militaries: PlayerMilitaries = PlayerMilitaries()
)
