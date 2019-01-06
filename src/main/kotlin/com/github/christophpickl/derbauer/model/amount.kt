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
            if (type == AmountType.Single) real
            else real - (real % type.thousands)

        private fun format(type: AmountType, rounded: Long): String =
            if (type == AmountType.Single) {
                rounded.toString()
            } else {
                val realCut = rounded / type.thousands
                "$realCut${type.sign}"
            }
    }

    @get:JsonIgnore val type: AmountType = whichType(real)
    @get:JsonIgnore val rounded: Long get() = round(type, real)
    @get:JsonIgnore val formatted: String get() = format(type, rounded)

    @get:JsonIgnore val isZero = real == 0L
    @get:JsonIgnore val isNotZero = real != 0L

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
            val hasAnyAnnotation = stackTraceElements.any { stack ->
                val method = Class.forName(stack.className).declaredMethods.first { it.name == stack.methodName }
                method.getAnnotation(AmountToStringAllowed::class.java) != null
            }
            
            val isLogStatement = stackTraceElements.any { it.className == "mu.internal.LocationAwareKLogger" }
            if (!hasAnyAnnotation && !isLogStatement) {
                throw UnsupportedOperationException(
                    "Method must be annotated with @${AmountToStringAllowed::class.simpleName} " +
                        "in order to use the Amount.toString()! " +
                        // 0 ... Thread.getStackTrace
                        // 1 ... Amount.toString
                        // 2 ... invoking method
                        "Invoking method was: ${stackTraceElements[2].toLabel()}")
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

inline fun <A : Any> Iterable<A>.sumBy(selector: (A) -> Amount): Amount {
    var sum = 0L
    for (element in this) {
        sum += selector(element).real
    }
    return Amount(sum)
}

enum class AmountType(
    val sign: String,
    thousandFactor: Int
) {
    Single("", 0),
    Kilo("k", 1),
    Mega("m", 2),
    Giga("g", 3),
    Tera("t", 4);

    companion object {
        val valuesButSingle get() = values().drop(1)
    }

    val regexp = Regex("""(\d+)($sign)""")
    val thousands = if (thousandFactor == 0) 0 else Math.pow(1_000.0, thousandFactor.toDouble()).toLong()
    val limit = Math.pow(1_000.0, thousandFactor + 1.0).toLong()
}

fun <E : Amountable> List<E>.realAmountSum(): Amount =
    Amount(map { it.amount.real }.sum())
