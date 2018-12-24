package com.github.christophpickl.derbauer2.endturn.happening

import com.github.christophpickl.derbauer2.CHEAT_MODE
import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.screen.InfoScreen
import com.github.christophpickl.derbauer2.ui.screen.Screen
import mu.KotlinLogging.logger
import kotlin.random.Random

class HappeningScreen(message: String) : InfoScreen(message) {
    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        Model.goHome()
    }
}

class Happener {

    private val log = logger {}
    private val happenings = listOf(
        GoldBagHappening(),
        RatsHappening()
    )
    private var turnsNothingHappened = 999
    private val baseProb = 0.1

    fun letItHappen(): Screen? {
        happenings.forEach { it.coolUpWarmUp() }

        val prob = Math.min(baseProb, (baseProb / 10 * turnsNothingHappened))
        log.trace { "Happening probability: $prob (turns quiet: $turnsNothingHappened)" }
        if (nextRandom0To1() < if (CHEAT_MODE) 0.1 else prob) {
            turnsNothingHappened = 0
            val happening = happenings.sortedBy { it.currentCooldown }.first()
            return HappeningScreen(happening.execute())
        }

        turnsNothingHappened++
        return null
    }

    private fun nextRandom0To1() = Random.nextDouble(0.0, 1.0)
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
