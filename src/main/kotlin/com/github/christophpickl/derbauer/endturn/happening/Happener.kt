package com.github.christophpickl.derbauer.endturn.happening

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.endturn.happening.happenings.GoldBagHappening
import com.github.christophpickl.derbauer.endturn.happening.happenings.RatsHappening
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.view.InfoView
import com.github.christophpickl.derbauer.ui.view.View
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
    private val baseProb = Values.happenings.baseProbability

    fun letItHappen(): View? {
        happenings.forEach { it.coolUpWarmUp() }

        val prob = Math.min(baseProb, (baseProb / Values.happenings.turnsCooldown * turnsNothingHappened))
        log.trace { "Happening probability: $prob (turns quiet: $turnsNothingHappened)" }
        if (Random.nextDouble() < prob) {
            turnsNothingHappened = 0
            val nature = determineNature()
            val happening = happenings.filter { it.nature == nature }.sortedBy { it.currentCooldown }.first()
            return HappeningView(happening.execute())
        }

        turnsNothingHappened++
        return null
    }

    private fun determineNature(): HappeningNature {
        val rand = Random.nextDouble() + Model.global.karma
        return if (rand >= 0.5) HappeningNature.Positive else HappeningNature.Negative
    }

}


abstract class Happening(
    val cooldownDays: Int,
    val nature: HappeningNature
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

enum class HappeningNature {
    Positive,
    Negative
}
