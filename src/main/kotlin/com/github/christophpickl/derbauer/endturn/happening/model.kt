package com.github.christophpickl.derbauer.endturn.happening

import com.github.christophpickl.derbauer.model.Percent
import mu.KotlinLogging.logger

interface Happening {
    val currentCooldownDays: Int
    val nature: HappeningNature

    fun probability(): Percent
    fun coolUpWarmUp()
    fun execute(): String
}

abstract class BaseHappening(
    val totalCooldownDays: Int,
    override val nature: HappeningNature
) : Happening {

    private val log = logger {}
    override var currentCooldownDays = 0

    final override fun probability(): Percent {
        return probabilityByCooldown() * additionalProbability()
    }

    protected open fun additionalProbability(): Double = 1.0

    private fun probabilityByCooldown(): Percent {
        val result = Percent(1 - (currentCooldownDays.toDouble() / totalCooldownDays))
        log.trace { "Probability based on cooldown: $result (currentDays: $currentCooldownDays / totalDays: $totalCooldownDays)" }
        return result
    }

    abstract fun internalExecute(): String

    override fun execute(): String {
        currentCooldownDays = totalCooldownDays
        return internalExecute()
    }

    override fun coolUpWarmUp() {
        if (currentCooldownDays > 0) {
            currentCooldownDays--
        }
    }

}

enum class HappeningNature {
    Positive,
    Negative
}
