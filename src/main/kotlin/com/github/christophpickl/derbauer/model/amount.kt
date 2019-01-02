package com.github.christophpickl.derbauer.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.DEV_MODE
import com.github.christophpickl.derbauer.misc.toLabel
import kotlin.math.roundToLong

interface Amountable {
    var amount: Amount
}

interface LimitedAmount : Amountable {
    val limitAmount: Amount
    val capacityLeft: Amount get() = limitAmount - amount
}

@Suppress("TooManyFunctions")
data class Amount(
    val real: Long
) {
    companion object {
        val zero = Amount(0)
        val one = Amount(1)
        fun minOf(first: Amount, second: Amount, vararg more: Amount) =
            listOf(first, second).plus(more.toList()).minBy { it.real }!!

        fun minOfNonNegative(first: Amount, second: Amount, vararg more: Amount) =
            Amount(listOf(first, second).plus(more.toList()).minBy { it.real }!!.real.coerceAtLeast(0))

        fun maxOf(first: Amount, second: Amount, vararg more: Amount) =
            listOf(first, second).plus(more.toList()).maxBy { it.real }!!

        private fun whichType(real: Long): AmountType {
            val realAbs = Math.abs(real)
            return AmountType.values().firstOrNull {
                realAbs < it.limit
            } ?: AmountType.values().last()
        }

        private fun round(type: AmountType, real: Long): Long =
            real - (real % type.thousands)

        private fun format(type: AmountType, rounded: Long): String {
            val realCut = rounded / type.thousands
            return "$realCut${type.sign}"
        }
    }

    val type: AmountType = whichType(real)
    val rounded: Long get() = round(type, real)
    val formatted: String get() = format(type, rounded)

    @JsonIgnore val isZero = real == 0L
    @JsonIgnore val isNotZero = real != 0L

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
            val stackTraceElements = Thread.currentThread().stackTrace.toList()
            // 0 ... Thread.getStackTrace
            // 1 ... Amount.toString
            val invokingTrace = stackTraceElements[2]
            val method = Class.forName(invokingTrace.className).methods.first { it.name == invokingTrace.methodName }
            val hasAnnotation = method.getAnnotation(AmountToStringAllowed::class.java) != null
            if (!hasAnnotation) {
                throw UnsupportedOperationException(
                    "Method must be annotated with @${AmountToStringAllowed::class.simpleName} in order to use the Amount.toString()! " +
                        "Invoking method was: ${invokingTrace.toLabel()}")
            }
        }
        return real.toString()
    }
}


/**
 * The toString method of [Amount] is prohibited without using this annotation.
 */
@Target(AnnotationTarget.FUNCTION)
annotation class AmountToStringAllowed

// TODO can not be imported?!
operator fun Int.times(amount: Amount) = Amount(amount.real * this)

inline fun <A : Any> Iterable<A>.sumBy(selector: (A) -> Amount): Amount {
    var sum = 0L
    for (element in this) {
        sum += selector(element).real
    }
    return Amount(sum)
}

// FIXME test me
enum class AmountType(
    val sign: String,
    val thousandFactor: Int
) {
    Single("", 0),
    Kilo("k", 1),
    Mega("m", 2),
    Giga("g", 3),
    Tera("t", 4);

    val thousands = Math.pow(1_000.0, thousandFactor.toDouble()).toLong()
    val limit = Math.pow(1_000.0, thousandFactor + 1.0).toLong()
}

fun <E : Amountable> List<E>.realAmountSum(): Amount =
    Amount(map { it.amount.real }.sum())
