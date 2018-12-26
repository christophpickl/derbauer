package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.data.Values

data class Global(
    var day: Int = 1,
    var reproductionRate: Double = Values.globals.reproductionRate,
    var peopleGoldRate: Double = Values.globals.peopleGoldRate
)

data class History(
    var attacked: Int = 0,
    var traded: Int = 0
)
