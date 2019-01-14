package com.github.christophpickl.derbauer.model

import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.amount.Amount

data class Global(
    var day: Int = 1,
    var reproductionRate: Double = Values.globals.initReproductionRate,
    var peopleGoldRate: Double = Values.globals.initPeopleGoldRate,
    var karma: Double = Values.karma.initial
)

data class History(
    var attacked: Int = 0,
    var traded: Int = 0
)

interface Wealthable {
    val wealth: Amount
}

interface Upkeepable {
    var upkeep: Amount
}
