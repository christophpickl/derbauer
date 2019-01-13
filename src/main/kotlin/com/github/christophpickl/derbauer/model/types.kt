package com.github.christophpickl.derbauer.model

import com.github.christophpickl.kpotpourri.common.string.IgnoreStringified


interface Entity : Labeled

interface Labeled {
    val label: String
}

interface MultiLabeled : Labeled {
    @IgnoreStringified
    val labelSingular: String
    @IgnoreStringified
    val labelPlural: String
    override val label get() = labelSingular

    fun labelByAmount(amount: Amount) = if (amount.real == 1L) labelSingular else labelPlural
}

interface Describable {
    val description: String
}

interface UsableEntity : Entity, Amountable {
    val usedAmount: Amount
    val unusedAmount: Amount get() = Amount(amount.real - usedAmount.real)
}

interface Ordered {
    @IgnoreStringified
    val order: Int
}

fun <E : Ordered> List<E>.ordered(): List<E> =
    sortedBy { it.order }

interface Conditional {
    fun checkCondition(): Boolean
}

fun <E : Entity> List<E>.filterConditional() = filter {
    if (it is Conditional) it.checkCondition() else true
}
