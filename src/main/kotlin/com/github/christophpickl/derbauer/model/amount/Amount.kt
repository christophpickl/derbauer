package com.github.christophpickl.derbauer.model.amount

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.DEV_MODE
import com.github.christophpickl.derbauer.misc.restrictMethodUsage
import com.github.christophpickl.kpotpourri.common.random.RealRandomService
import kotlin.math.roundToLong

@Suppress("TooManyFunctions")
data class Amount(
    val real: Long
) {

    constructor(real: Int) : this(real.toLong())

    companion object :
        AmountParser by AmountParserImpl,
        AmountRounder by AmountRounderImpl,
        AmountFormatter by AmountFormatterImpl {

        val zero = Amount(0)
        val one = Amount(1)
        val two = Amount(2)

        fun minOf(first: Amount, second: Amount, vararg more: Amount) =
            listOf(first, second).plus(more.toList()).minBy { it.real }!!

        fun minOfNonNegative(first: Amount, second: Amount, vararg more: Amount) =
            Amount(listOf(first, second).plus(more.toList()).minBy { it.real }!!.real.coerceAtLeast(0))

        fun maxOf(first: Amount, second: Amount, vararg more: Amount) =
            listOf(first, second).plus(more.toList()).maxBy { it.real }!!

    }

    @get:JsonIgnore val type: AmountType = AmountType.byReal(real)
    @get:JsonIgnore val rounded: Long get() = round(type, real)
    @get:JsonIgnore val formatted: String get() = format(real, type, rounded)

    @get:JsonIgnore val isZero = real == 0L
    @get:JsonIgnore val isNotZero = real != 0L
    @get:JsonIgnore val isNegative = real < 0L

    operator fun plus(other: Amount) = Amount(real + other.real)
    operator fun minus(other: Amount) = Amount(real - other.real)
    operator fun plus(plusReal: Long) = Amount(real + plusReal)
    operator fun minus(minusReal: Long) = Amount(real - minusReal)
    operator fun inc() = Amount(real + 1)
    operator fun dec() = Amount(real - 1)
    operator fun compareTo(other: Amount) = real.compareTo(other.real)
    operator fun compareTo(other: Long) = real.compareTo(other)
    operator fun times(multiplicator: Double) = Amount((real * multiplicator).roundToLong())
    operator fun times(multiplicator: Long) = Amount(real * multiplicator)
    operator fun times(multiplicator: Amount) = Amount(real * multiplicator.real)
    operator fun div(divisor: Amount) = Amount(real / divisor.real)
    operator fun div(divisor: Long) = Amount(real / divisor)
    operator fun unaryMinus() = Amount(-real)

    override fun toString(): String {
        if (DEV_MODE) {
            restrictMethodUsage(
                basePackageName = "com.github.christophpickl.derbauer",
                requiredAnnotation = AmountToStringAllowed::class
            )
        }
        return real.toString()
    }

}

fun Collection<Amount>.summed(): Long = sumBy { it }.real


private val random = RealRandomService

data class AmountDistribution(
    val multiplyBy: Int,
    val randLower: Double,
    val randUpper: Double
) {

    companion object {
        val zero = AmountDistribution(0, 0.0, 0.0)
    }

    fun compute(base: Amount) = if (base.isZero || this === zero) Amount.zero else
        Amount(random.randomize(base.real * multiplyBy, randLower, randUpper))

}
