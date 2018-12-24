package com.github.christophpickl.derbauer.happening

import com.github.christophpickl.derbauer.logic.Screen
import com.github.christophpickl.derbauer.logic.ScreenCallback
import com.github.christophpickl.derbauer.logic.SimpleMessageScreen
import com.github.christophpickl.derbauer.model.DEPRECATED_CHEAT_MODE
import mu.KotlinLogging.logger
import kotlin.random.Random

class HappeningScreen(message: String) : SimpleMessageScreen(message) {
    override fun onCallback(callback: ScreenCallback) {
        callback.onHappening(this)
    }
}

class Happener {

    private val log = logger {}
    private val happenings = listOf(
        GoldBagHappening(),
        RatsHappening()
    )
    private var turnsNothingHappened = 999
    private val baseProb = 10.0

    fun letItHappen(): Screen? {
        happenings.forEach { it.coolUpWarmUp() }

        val prob = if (DEPRECATED_CHEAT_MODE) 100.0 else Math.min(baseProb, (baseProb / 10 * turnsNothingHappened))
        log.trace { "Happening probability: $prob (turns quiet: $turnsNothingHappened)" }
        if (nextRandom0To100() < prob) {
            turnsNothingHappened = 0
            val happening = happenings.sortedBy { it.currentCooldown }.first()
            return HappeningScreen(happening.execute())
        }

        turnsNothingHappened++
        return null
    }

    private fun nextRandom0To100() = Random.nextDouble(0.0, 100.0)
}

abstract class Happening(
    val cooldownDays: Int
) {

    var currentCooldown = 0

    abstract fun internalExecute(): String

    fun execute(): String {
        currentCooldown = cooldownDays
        return internalExecute()
    }

    fun coolUpWarmUp() {
        if (currentCooldown > 0) {
            currentCooldown--
        }
    }
}
