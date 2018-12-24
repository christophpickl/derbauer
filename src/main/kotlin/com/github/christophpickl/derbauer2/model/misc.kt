package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.trade.Buyable

data class Global(
    var day: Int = 1,
    var reproductionRate: Double = 0.5,
    var peopleGoldRate: Double = 0.4
)

interface Labeled {
    val label: String
}

interface MultiLabeled {
    val labelSingular: String
    val labelPlural: String
}

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

// TODO also apply to trade resources
interface Amountable {
    var amount: Int
}

interface LimitedAmount : Amountable {
    val limitAmount: Int
    val capacityLeft: Int get() = limitAmount - amount
}

interface LimitedBuyableAmount : LimitedAmount, Buyable {
    override val effectiveBuyPossibleAmount get() = Math.min(buyPossibleAmount, capacityLeft)
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
