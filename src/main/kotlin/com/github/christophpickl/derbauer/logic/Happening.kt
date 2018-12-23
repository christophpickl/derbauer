package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.model.CHEAT_MODE
import com.github.christophpickl.derbauer.model.State
import mu.KotlinLogging.logger
import javax.inject.Inject
import kotlin.random.Random

class EndTurnHappener @Inject constructor(
    private val state: State,
    private val achievement: AchievementHappener
) {

    private val log = logger {}
    private val randomHappenings = listOf(
        GoldBagHappening(state),
        RatsHappening(state)
    )
    private var turnsNothingHappened = 999
    private val baseProb = 10.0

    fun letItHappen(): String? {
        achievement.anyHappened()?.let {
            return it
        }

        val prob = Math.min(baseProb, (baseProb / 10 * turnsNothingHappened))
        log.trace { "Happening probability: $prob (turns quiet: $turnsNothingHappened)" }
        if (nextRandom0To100() < prob) {
            turnsNothingHappened = 0
            val happening = randomHappenings.random()
            return happening.execute()
        }

        turnsNothingHappened++
        return null
    }

    private fun nextRandom0To100() = Random.nextDouble(0.0, 100.0)
}

class AchievementHappener @Inject constructor(
    private val state: State
) {

    private val allAchievements: List<Achievement> = listOf(
        Trade1Achievement(state),
        Army1Achievement(state)
    )

    fun anyHappened(): String? {
        val achievements = allAchievements.mapNotNull { it.check() }
        return if (achievements.isEmpty()) null else {
            "Congrats, new achievement${if (achievements.size == 1) "" else "s"} unlocked:\n" +
                achievements.joinToString("\n")
        }
    }

}

class Trade1Achievement(private val state: State) : Achievement(message = "Trade Mastery I (cheaper trade rates)") {
    private val tradeThreshold = if (CHEAT_MODE) 1 else 10

    override fun condition() =
        state.history.traded >= tradeThreshold

    override fun changeState() {
        state.prices.trade.decreaseAllBy(0.2)
    }

}

class Army1Achievement(private val state: State) : Achievement(message = "Military Mastery I (soldier attack +30%)") {
    private val attackThreshold = if (CHEAT_MODE) 1 else 10

    override fun condition() =
        state.history.attacked >= attackThreshold

    override fun changeState() {
        state.army.soldierAttackStrength += 0.3
    }

}

abstract class Achievement(
    val message: String
) {
    private var isAchieved = false
    protected abstract fun condition(): Boolean
    protected abstract fun changeState()

    fun check(): String? {
        if (!isAchieved && condition()) {
            isAchieved = true
            changeState()
            return message
        }
        return null
    }
}


private class GoldBagHappening(
    private val state: State
) : Happening() {
    private val goldBagSizes = listOf(10, 20, 50)
    override fun execute(): String {
        val bagSize = goldBagSizes.random()
        state.player.gold += bagSize
        return "You were lucky, while walking in the forrest you found a treasure: +$bagSize Gold"
    }
}

private class RatsHappening(
    private val state: State
) : Happening() {

    private val eatenSizes = listOf(10, 20, 30)

    override fun execute(): String {
        val foodEaten = eatenSizes.random()
        state.player.food -= foodEaten
        return "Oh noes, some smelly rats ate some of your foodz: -$foodEaten Food"
    }

}

private abstract class Happening {
    abstract fun execute(): String
}
