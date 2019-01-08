package com.github.christophpickl.derbauer.endturn.happening

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.endturn.happening.happenings.GoldBagHappening
import com.github.christophpickl.derbauer.endturn.happening.happenings.RatsHappening
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.view.InfoView
import com.github.christophpickl.derbauer.ui.view.View
import com.github.christophpickl.kpotpourri.common.random.RandomService
import com.github.christophpickl.kpotpourri.common.random.RealRandomService
import mu.KotlinLogging.logger

class HappeningView(message: String) : InfoView(message) {
    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        Model.goHome()
    }
}

class Happener(
    private val random: RandomService = RealRandomService,
    private val happenings: List<Happening> = listOf(
        GoldBagHappening(),
        RatsHappening()
    )
) {

    private val log = logger {}

    private var turnsNothingHappened = 999
    private val baseProb = Values.happenings.baseProbability

    fun letItHappen(): View? {
        happenings.forEach { it.coolUpWarmUp() }

        val prob = Math.min(baseProb, (baseProb / Values.happenings.turnsCooldown * turnsNothingHappened))
        log.trace { "Happening probability: $prob (turns quiet: $turnsNothingHappened)" }
        if (random.nextDouble(0.0, 1.0) < prob) {
            turnsNothingHappened = 0
            val nature = determineNature()
            val happening = happenings.filter { it.nature == nature }.sortedByDescending { it.probability() }.first()
            log.debug { "Happening happening: $happening (nature: $nature)" }
            return HappeningView(happening.execute())
        }

        log.debug { "Increasing number of turns nothing happened." }
        turnsNothingHappened++
        return null
    }

    private fun determineNature(): HappeningNature {
        val rand = random.nextDouble(0.0, 1.0) + Model.global.karma
        return if (rand >= 0.5) HappeningNature.Positive else HappeningNature.Negative
    }

}
