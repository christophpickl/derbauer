package com.github.christophpickl.derbauer.model

import mu.KotlinLogging.logger

// TODO use it more often
data class Percent(
    val value: Double
) : Comparable<Percent> {

    companion object {
        private val log = logger {}
        val yes = Percent(1.0)
        val no = Percent(0.0)
        fun coerceWithin(value: Double): Percent {
            val result = Percent(Math.max(Math.min(value, 1.0), 0.0))
            if (result.value != value) {
                log.debug { "Coerced percentage value: $value to $result" }
            }
            return result
        }
    }

    init {
        require(value in 0.0..1.0) {
            "Value must be within 0.0 and 1.0 but was: $value"
        }
    }

    override fun compareTo(other: Percent) = value.compareTo(other.value)

    operator fun times(multiplicator: Double) = Percent.coerceWithin(value * multiplicator)
    operator fun times(multiplicator: Percent) = this * multiplicator.value

    override fun toString() = String.format("%.2f", value)

}
