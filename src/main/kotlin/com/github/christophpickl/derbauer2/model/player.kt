package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.build.PlayerBuildings

data class Player(
    val resources: PlayerResources = PlayerResources(),
    val buildings: PlayerBuildings = PlayerBuildings()
)
