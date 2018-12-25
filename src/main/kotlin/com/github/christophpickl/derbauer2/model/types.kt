package com.github.christophpickl.derbauer2.model

import com.github.christophpickl.derbauer2.misc.IgnoreStringified


interface Entity : Labeled, Ordered

interface Labeled {
    val label: String
}

interface MultiLabeled : Labeled {
    @IgnoreStringified
    val labelSingular: String
    @IgnoreStringified
    val labelPlural: String
    override val label get() = labelSingular
}

interface Descriptable {
    val description: String
}

interface Amountable {
    var amount: Int
}

fun <E : Amountable> List<E>.amountsSum(): Int =
    map { it.amount }.sum()


interface LimitedAmount : Amountable {
    val limitAmount: Int
    val capacityLeft: Int get() = limitAmount - amount
}

interface UsableEntity : Entity, Amountable {
    val usedAmount: Int
    val unusedAmount get() = amount - usedAmount
}

interface Ordered {
    @IgnoreStringified
    val order: Int
}

fun <E : Ordered> List<E>.ordered(): List<E> =
    sortedBy { it.order }

interface ConditionalEntity {
    fun checkCondition(): Boolean
}

fun <E : Entity> List<E>.filterConditional() = filter {
    if (it is ConditionalEntity) it.checkCondition() else true
}
