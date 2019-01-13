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
            else {
                val rest = real % type.thousands
                val rawRounded = real - rest
                val realCut = rawRounded / type.thousands
                println("ROUND: rest=$rest, realCut=$realCut")
                val absRealCut = Math.abs(realCut)
                val absRest = Math.abs(rest)
                val absReal = Math.abs(real)
                val addition = when {
                    absRealCut < 10 -> if (rest == 0L) 0 else {
                        absRest.toString().let {
                            if (absReal.toString().elementAt(1) == '0') "0$it" else it
                        }.substring(0, 2).toInt() * 10 * type.thousandsForNext
                    }
                    absRealCut < 100 -> absRest.toString().substring(0, 1).toInt() * 100 * type.thousandsForNext
                    else -> 0
                }
                println("ROUND: real=$real, rest=$rest; realCut=$realCut; type.thousandsForNext=${type.thousandsForNext}")
                println("ROUND: rawRounded=$rawRounded; addition: $addition")
                rawRounded + (addition * (if (real < 0) -1 else 1))
            }

        private fun format(real: Long, type: AmountType, rounded: Long): String =
            if (type == AmountType.Single) {
                rounded.toString()
            } else {
                val realCut = rounded / type.thousands
                val absRealCut = Math.abs(realCut)
                val absReal = Math.abs(real)
                val floatPart = when {
                    absRealCut < 10 -> { // real=1239; rounded=1230, realCut=1 ===> 1239.substring=239, take2=23 => 1.23
                        val rest = absReal.toString().substring(1).take(2)
                        println("FORMAT: rest=$rest")
                        ".$rest"
                    }
                    absRealCut < 100 -> // real=12399; realCut=12 ===> realCut.length=2, real.sub(2)=399, first=3 => 12.3
                        ".${absReal.toString().substring(2).take(1)}"
                    else -> ""
                }
                println("FORMAT: rounded=$rounded, real=$real, floatPart=$floatPart; realCut=$realCut")
                "$realCut$floatPart${type.sign}"
            }
    }

    @get:JsonIgnore val type: AmountType = whichType(real)
    @get:JsonIgnore val rounded: Long get() = round(type, real)
    @get:JsonIgnore val formatted: String get() = format(real, type, rounded)

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

            val hasAnyAnnotation = stackTraceElements
                .filter { it.className.startsWith("com.github.christophpickl.derbauer") }
                .any { stack ->
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
    val thousandsForNext = if (thousandFactor == 0) 1 else Math.pow(1_000.0, (thousandFactor - 1).toDouble()).toLong()
    val limit = Math.pow(1_000.0, thousandFactor + 1.0).toLong()
}

fun <E : Amountable> List<E>.realAmountSum(): Amount =
    Amount(map { it.amount.real }.sum())
