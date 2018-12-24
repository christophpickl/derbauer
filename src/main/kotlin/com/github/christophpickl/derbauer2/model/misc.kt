package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.misc.Stringifier

data class Global(
    var day: Int = 1
// TODO reproductionRate
)

interface Labeled {
    val labelSingular: String
    val labelPlural: String
}

enum class BuySell(val label: String) {
    Buy("buy"),
    Sell("bell")
}

class History(
    var attacked: Int = 0,
    var traded: Int = 0
) {
    override fun toString() = Stringifier.stringify(this)
}

interface Buyable {
    var buyPrice: Int
}

// TODO also apply to trade resources
interface Amountable {
    var amount: Int
}
