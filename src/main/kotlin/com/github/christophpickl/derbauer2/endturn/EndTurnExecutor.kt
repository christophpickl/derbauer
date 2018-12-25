package com.github.christophpickl.derbauer2.endturn

import com.github.christophpickl.derbauer2.model.Model
import mu.KotlinLogging.logger
import kotlin.random.Random

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
        Model.actions.visitorsWaitingInThroneRoom += Random.nextInt(0, Math.max(2, (Model.people / 100.0 * 1).toInt()))

        Model.features.all.forEach {
            it.isEnabled() // check to get notifications here
        }
        val notifications = Model.notifications.consumeAll()
        
        return EndTurnReport(
            goldIncome = goldIncome,
            foodIncome = foodIncome,
            peopleIncome = peopleIncome,
            notifications = notifications
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
            if (loosingPeople == 0) loosingPeople = Random.nextInt(0, 3)
            log.trace { "loosing people because empty food: $loosingPeople (percentage: $percentageLoose%)" }
            calc -= loosingPeople
        } else {
            var reproduced = (Model.people * Model.global.reproductionRate).toInt()
            reproduced = (reproduced * Random.nextDouble(0.7, 1.2)).toInt()
            calc += reproduced
        }
        if (Model.people < 20 && Random.nextDouble(0.0, 1.0) < 0.3) {
            log.trace { "people randomly added" }
            calc += Random.nextInt(2, 5)
        }
        if (Model.people > 10 && Random.nextDouble(0.0, 1.0) < 0.1) {
            log.trace { "people randomly killed" }
            calc -= Random.nextInt(2, 6)
        }
        calc = limitCalcMax(calc, Model.people, Model.totalPeopleCapacity)
        log.trace { "People calc: $calc (people=${Model.people}, food=${Model.food}, reproRate=${Model.global.reproductionRate}, cap=${Model.totalPeopleCapacity})" }
        return calc
    }

    private fun calcGold(): Int {
        var calc = (Model.people * Model.global.peopleGoldRate).toInt()
        calc = (calc * Random.nextDouble(0.8, 1.6)).toInt()
        log.trace { "Gold calc: $calc (people=${Model.people} * rate=${Model.global.peopleGoldRate})" }
        return calc
    }

    private fun limitCalcMax(calc: Int, current: Int, max: Int) =
        if (current + calc <= max) calc else max - current

    private fun limitCalcMin(calc: Int, current: Int) =
        if (current + calc >= 0) calc else -current

}
