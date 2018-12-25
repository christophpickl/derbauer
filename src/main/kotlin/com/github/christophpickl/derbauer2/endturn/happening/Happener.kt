package com.github.christophpickl.derbauer2.endturn.happening

import com.github.christophpickl.derbauer2.VALUES
import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.view.InfoView
import com.github.christophpickl.derbauer2.ui.view.View
import mu.KotlinLogging.logger
import kotlin.random.Random

class HappeningView(message: String) : InfoView(message) {
    override fun onCallback(callback: ViewCallback, input: PromptInput) {
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
    private val baseProb = VALUES.happeningBaseProbability

    fun letItHappen(): View? {
        happenings.forEach { it.coolUpWarmUp() }

        val prob = Math.min(baseProb, (baseProb / VALUES.happeningTurnsCooldown * turnsNothingHappened))
        log.trace { "Happening probability: $prob (turns quiet: $turnsNothingHappened)" }
        if (Random.nextDouble(0.0, 1.0) < prob) {
            turnsNothingHappened = 0
            val happening = happenings.sortedBy { it.currentCooldown }.first()
            return HappeningView(happening.execute())
        }

        turnsNothingHappened++
        return null
    }

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
