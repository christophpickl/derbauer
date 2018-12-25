package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.misc.Stringifier

data class Global(
    var day: Int = 1,
    var reproductionRate: Double = 0.5,
    var peopleGoldRate: Double = 0.4
)


enum class BuySell(val label: String) {
    Buy("buy"),
    Sell("sell")
}

class History(
    var attacked: Int = 0,
    var traded: Int = 0
) {
    override fun toString() = Stringifier.stringify(this)
}

fun <E : Ordered> List<E>.ordered(): List<E> =
    sortedBy { it.order }

interface Ordered {
    val order: Int
}
