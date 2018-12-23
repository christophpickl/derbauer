package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.logic.screens.EndTurnScreen
import com.github.christophpickl.derbauer.logic.screens.GameOverScreen
import com.github.christophpickl.derbauer.logic.screens.Screen
import com.github.christophpickl.derbauer.model.State
import javax.inject.Inject
import kotlin.random.Random

class TurnFinisher @Inject constructor(
    private val state: State,
    private val happener: EndTurnHappener
) {

    private val numberWidth = 6
    private val numberWidthGrow = 5 // plus sign

    fun calculateEndTurn(): Screen {
        val maybeHappenedMessage = happener.letItHappen()

        val peopleIncome = calcPeople()
        val foodIncome = calcFood()
        val goldIncome = calcGold()

        val message = """Your daily end turn report:
            |
            |${formatGrowth("Food production", state.player.food, foodIncome)}
            |${formatGrowth("People growth  ", state.player.people, peopleIncome)}
            |${formatGrowth("Gold income    ", state.player.gold, goldIncome)}
            | 
            |${maybeHappenedMessage ?: "It was quiet and calm."}
            |
            |Hit ENTER to continue.
        """.trimMargin()
        state.player.food += foodIncome
        state.player.people += peopleIncome
        state.player.gold += goldIncome

        if (state.player.people <= 0) {
            return GameOverScreen()
        }
        return EndTurnScreen(message)
    }

    private fun calcFood(): Int {
        return (state.player.buildings.farms * state.buildings.farmProduces) - // farm production 
            state.player.people // each persons eats one food per day
    }

    private fun calcPeople(): Int {
        var calc = 0
        val food = state.player.food
        if (food < -10) {
            calc += food / 10 // lose one people per -x food
        } else if (food >= 500) {
            calc += food / 500 // gain one people per +x food
        }
        calc += (state.player.people * state.meta.reproductionRate).toInt() // reproduction rate by x% people
        if (Random.nextDouble(0.0, 1.0) < 0.3) {
            calc += 1
        }
        return if (state.player.people + calc <= state.playerPeopleMax) calc else state.playerPeopleMax - state.player.people
    }

    private fun calcGold(): Int {
        return (state.player.people * 0.5).toInt()
    }

    private fun formatGrowth(label: String, current: Int, growth: Int) =
        "$label: ${formatNumber(current, numberWidth)} => " +
            "${formatNumber(growth, numberWidthGrow, addPlusSign = true)} => " +
            formatNumber(current + growth, numberWidth)

}
