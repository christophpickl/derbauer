package com.github.christophpickl.derbauer.model.amount


interface Amountable {
    var amount: Amount
}

interface LimitedAmount : Amountable {
    val limitAmount: Amount
    val capacityLeft: Amount get() = limitAmount - amount
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
        fun byReal(real: Long): AmountType {
            val realAbs = Math.abs(real)
            return AmountType.values().firstOrNull {
                realAbs < it.limit
            } ?: AmountType.values().last()
        }

        val valuesButSingle get() = values().drop(1)
    }

    val regexp = Regex("""(\d+)(\.\d+)?($sign)""")
    val thousands = if (thousandFactor == 0) 0 else Math.pow(1_000.0, thousandFactor.toDouble()).toLong()
    val thousandsForNext = if (thousandFactor == 0) 1 else Math.pow(1_000.0, (thousandFactor - 1).toDouble()).toLong()
    val limit = Math.pow(1_000.0, thousandFactor + 1.0).toLong()
}

fun <E : Amountable> List<E>.realAmountSum(): Amount =
    Amount(map { it.amount.real }.sum())
