package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.Values

data class Global(
    var day: Int = 1,
    var reproductionRate: Double = 0.5,
    var peopleGoldRate: Double = 0.4,
    var visitorsWaitingInThroneRoom: Int = Values.actions.visitorsWaitingInThroneRoom
)

data class History(
    var attacked: Int = 0,
    var traded: Int = 0
)
