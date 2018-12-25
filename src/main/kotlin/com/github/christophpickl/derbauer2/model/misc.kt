package com.github.christophpickl.derbauer2.model

data class Global(
    var day: Int = 1,
    var reproductionRate: Double = 0.5,
    var peopleGoldRate: Double = 0.4
)

data class History(
    var attacked: Int = 0,
    var traded: Int = 0
)
