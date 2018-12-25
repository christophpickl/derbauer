package com.github.christophpickl.derbauer2.endturn

import com.github.christophpickl.derbauer2.model.Model
import mu.KotlinLogging.logger
import kotlin.random.Random

data class EndTurnReport(
    val goldIncome: Int,
    val foodIncome: Int,
    val peopleIncome: Int
)
object EndTurnExecutor {

    private val log = logger {}

    fun execute(): EndTurnReport {
        val peopleIncome = calcPeople() // depends on food
        val foodIncome = calcFood() // depends on people
        val goldIncome = calcGold() // depends on people

        Model.gold += goldIncome
        Model.food += foodIncome
        Model.people += peopleIncome

        Model.global.day++
        Model.global.visitorsWaitingInThroneRoom += Random.nextInt(0, Math.max(2, (Model.people / 100.0 * 1).toInt()))

        return EndTurnReport(
            goldIncome = goldIncome,
            foodIncome = foodIncome,
            peopleIncome = peopleIncome
        )
    }

    private fun calcFood(): Int {
        var calc = Model.player.buildings.totalFoodProduction
        calc -= Model.people // each persons eats one food per day

        calc = limitCalcMax(calc, Model.food, Model.totalFoodCapacity)
        calc = limitCalcMin(calc, Model.food)
        log.trace { "Food calc: $calc (prod: ${Model.player.buildings.totalFoodProduction}, cap: ${Model.totalFoodCapacity}, people: ${Model.people})" }
        return calc
    }

    private fun calcPeople(): Int {
        var calc = 0
        if (Model.food == 0) {
            val percentageLoose = Random.nextInt(8, 12)
            var loosingPeople = (Model.people / 100.0 * percentageLoose).toInt()
            if (loosingPeople == 0) loosingPeople = Random.nextInt(1, 4)
            log.trace { "loosing people because empty food: $loosingPeople (percentage: $percentageLoose%)" }
            calc -= loosingPeople
        } else {
            calc += (Model.people * Model.global.reproductionRate).toInt() // reproduction rate by x% people
        }
        if (calc == 0 && Random.nextDouble(0.0, 1.0) < 0.3) {
            log.trace { "people randomly added" }
            calc += Random.nextInt(1, 4)
        }
        if (calc == 0 && Random.nextDouble(0.0, 1.0) < 0.2) {
            log.trace { "people randomly killed" }
            calc -= Random.nextInt(1, 4)
        }
        calc = limitCalcMax(calc, Model.people, Model.totalPeopleCapacity)
        log.trace { "People calc: $calc (people=${Model.people}, food=${Model.food}, reproRate=${Model.global.reproductionRate}, cap=${Model.totalPeopleCapacity})" }
        return calc
    }

    private fun calcGold(): Int {
        val income = (Model.people * Model.global.peopleGoldRate).toInt()
        log.trace { "Gold calc: $income (people=${Model.people} * rate=${Model.global.peopleGoldRate})" }
        return income
    }

    private fun limitCalcMax(calc: Int, current: Int, max: Int) =
        if (current + calc <= max) calc else max - current

    private fun limitCalcMin(calc: Int, current: Int) =
        if (current + calc >= 0) calc else -current

}
