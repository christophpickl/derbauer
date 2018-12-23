package com.github.christophpickl.derbauer.logic.service

import com.github.christophpickl.derbauer.logic.formatNumber
import com.github.christophpickl.derbauer.logic.screens.GameOverScreen
import com.github.christophpickl.derbauer.logic.screens.Screen
import com.github.christophpickl.derbauer.logic.screens.ScreenCallback
import com.github.christophpickl.derbauer.model.State
import javax.inject.Inject
import kotlin.random.Random


class EndTurnScreen(
    override val message: String
) : Screen {

    override val enableCancelOnEnter = false

    override fun onCallback(callback: ScreenCallback) {
        callback.onEndTurn(this)
    }
}

class EndTurn @Inject constructor(
    private val state: State
) {

    private val numberWidth = 6
    private val numberWidthGrow = 5 // plus sign

    fun calculateEndTurn(): Screen {
        val peopleIncome = calcPeople()
        val foodIncome = calcFood()
        val goldIncome = calcGold()

        val message = """So, this is what happened over night:
            |
            |${formatGrowth("Food production", state.player.food, foodIncome)}
            |${formatGrowth("People growth  ", state.player.people, peopleIncome)}
            |${formatGrowth("Gold income    ", state.player.gold, goldIncome)}
            | 
            |Hit ENTER to go on with your miserable existence.
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
        val calc = (state.player.buildings.farms * state.buildings.farmProduces) - // farm production 
            state.player.people // each persons eats one food per day
        return limitCalc(calc, state.player.food, state.maxFood)
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
        if (calc == 0 && Random.nextDouble(0.0, 1.0) < 0.3) {
            calc += 1
        }
        return limitCalc(calc, state.player.people, state.maxPeople)
    }

    private fun limitCalc(calc: Int, current: Int, max: Int) =
        if (calc + current <= max) calc else max - current

    private fun calcGold(): Int {
        return (state.player.people * 0.5).toInt()
    }

    private fun formatGrowth(label: String, current: Int, growth: Int) =
        "$label: ${formatNumber(current, numberWidth)} => " +
            "${formatNumber(growth, numberWidthGrow, addPlusSign = true)} => " +
            formatNumber(current + growth, numberWidth)

}
