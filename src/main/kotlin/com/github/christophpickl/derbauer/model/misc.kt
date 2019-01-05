package com.github.christophpickl.derbauer.model

import com.github.christophpickl.derbauer.data.Values

data class Global(
    var day: Int = 1,
    var reproductionRate: Double = Values.globals.reproductionRate,
    var peopleGoldRate: Double = Values.globals.peopleGoldRate,
    var karma: Double = Values.karma.initial
)

data class History(
    var attacked: Int = 0,
    var traded: Int = 0
)

interface Wealthable {
    val wealth: Amount
}
