package com.github.christophpickl.derbauer2.endturn

import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.Formatter
import mu.KotlinLogging.logger
import kotlin.random.Random

object EndTurnExecutor {

    private val log = logger {}
    private val numberWidth = 6
    private val numberWidthGrow = 5 // plus sign

    fun execute(): String {
        Model.global.day++

        val goldIncome = calcGold()
        val foodIncome = calcFood()
        val peopleIncome = calcPeople()

        val message = """
            So, this is what happened over night:
            
            ${formatGrowth("Gold income    ", Model.gold, goldIncome)}
            ${formatGrowth("Food production", Model.food, foodIncome)}
            ${formatGrowth("People growth  ", Model.people, peopleIncome)}
             
            Go on and continue your miserable existence.
            """.trimIndent()

        Model.gold += goldIncome
        Model.food += foodIncome
        Model.people += peopleIncome

        return message
    }

    private fun calcFood(): Int {
        val calc = Model.player.buildings.totalFoodProduction - Model.people // each persons eats one food per day
        val income = limitCalc(calc, Model.food, Model.totalFoodCapacity)
        log.trace { "Food income: income = ${Model.player.buildings.totalFoodProduction} - ${Model.people}" }
        return income
    }

    private fun calcPeople(): Int {
        var calc = 0
        val food = Model.food
        if (food < -10) {
            log.trace { "losing some people because of starvation." }
            calc += food / 10 // lose one people per -x food
        } else if (food >= 500) {
            log.trace { "losing some people because of HUGE starvation!" }
            calc += food / 500 // gain one people per +x food
        }
        // ... if over capacity! decrease!
        if (food > 0) {
            calc += (Model.people * Model.global.reproductionRate).toInt() // reproduction rate by x% people
        }
        if (calc == 0 && Random.nextDouble(0.0, 1.0) < 0.3) {
            log.trace { "Adding random people because you are so poor." }
            calc += 1
        }
        if (calc == 0 && Random.nextDouble(0.0, 1.0) < 0.2) {
            log.trace { "Losing random people." }
            calc -= 1
        }
        return limitCalc(calc, Model.people, Model.totalPeopleCapacity)
    }

    private fun calcGold(): Int {
        val income = (Model.people * Model.global.peopleGoldRate).toInt()
        log.trace { "Gold income: $income = (${Model.people} * ${Model.global.peopleGoldRate})" }
        return income
    }

    private fun limitCalc(calc: Int, current: Int, max: Int) =
        if (calc + current <= max) calc else max - current

    private fun formatGrowth(label: String, current: Int, growth: Int) =
        "$label: ${Formatter.formatNumber(current, numberWidth)} => " +
            "${Formatter.formatNumber(growth, numberWidthGrow, addPlusSign = true)} => " +
            Formatter.formatNumber(current + growth, numberWidth)
}
