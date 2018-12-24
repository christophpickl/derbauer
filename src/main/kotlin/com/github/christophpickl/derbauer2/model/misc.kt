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

// TODO also apply to trade resources
interface Amountable {
    var amount: Int
}

interface LimitedAmount : Amountable {
    val limitAmount: Int
    val capacityLeft: Int get() = limitAmount - amount
}

interface UsableResource : Amountable {
    val unusedAmount: Int
    val usedAmount: Int
}

interface Descriptable {
    val description: String
}

fun <E : Ordered> List<E>.ordered(): List<E> =
    sortedBy { it.order }

interface Ordered {
    val order: Int
}
