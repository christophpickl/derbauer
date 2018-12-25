package com.github.christophpickl.derbauer2.model


interface Entity : Labeled, Ordered

interface Labeled {
    val label: String
}

interface MultiLabeled : Labeled {
    val labelSingular: String
    val labelPlural: String
    override val label get() = labelSingular
}

interface Descriptable {
    fun description(): String
}

interface Amountable {
    var amount: Int
}

interface LimitedAmount : Amountable {
    val limitAmount: Int
    val capacityLeft: Int get() = limitAmount - amount
}

interface UsableEntity : Entity, Amountable {
    val usedAmount: Int
    val unusedAmount get() = amount - usedAmount
}

interface ConditionalEntity {
    fun checkCondition(): Boolean
}
