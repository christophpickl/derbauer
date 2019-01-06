package com.github.christophpickl.derbauer.resource

import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.random.RandomService
import com.github.christophpickl.kpotpourri.common.random.RealRandomService
import mu.KotlinLogging.logger

data class ResourceEndTurnReport(
    val goldIncome: Amount,
    val foodIncome: Amount,
    val peopleIncome: Amount
)

class ResourceEndTurn(
    private val random: RandomService = RealRandomService
) {

    private val log = logger {}

    fun endTurn() = ResourceEndTurnReport(
        goldIncome = calcGold(),
        foodIncome = calcFood(),
        peopleIncome = calcPeople()
    )

    private fun calcFood(): Amount {
        var calc = Model.player.buildings.totalFoodProduction

        // each persons eats one food per day
        calc -= Model.people

        if (calc + Model.food > Model.totalFoodCapacity) {
            // was already more food stored than capacity allowed => degrade!
            if (Model.food > Model.totalFoodCapacity) {
                calc = Amount(-(Math.max(10, (Model.totalFoodCapacity.real / 100.0 * 20).toLong())))
            } else {
                // do not produce more than capacity allows
                calc = Model.totalFoodCapacity - Model.food
            }
        }

        // can not lose more than actually stored
        if (calc < 0 && Model.food + calc < 0) {
            calc = -Model.food
        }

        log.trace {
            "Food calc: $calc (" +
                "current: ${Model.food}, " +
                "cap: ${Model.totalFoodCapacity}, " +
                "prod: ${Model.player.buildings.totalFoodProduction}, " +
                "people: ${Model.people}" +
                ")"
        }
        return calc
    }

    private fun calcPeople(): Amount {
        var calc = Amount.zero

        // no food => people dying
        if (Model.food.isZero) {
            val percentageLoose = random.nextInt(8, 12)
            var loosingPeople = (Model.people.real / 100.0 * percentageLoose).toLong()
            if (loosingPeople == 0L) loosingPeople = random.nextLong(1, 3)
            log.trace { "loosing people because empty food: $loosingPeople (percentage: $percentageLoose%)" }
            calc -= loosingPeople
        } else {
            // regular reproduction
            var reproduced = Model.people * Model.global.reproductionRate
            reproduced *= random.nextDouble(0.7, 1.2)
            calc += reproduced
        }

        calc = addOrKillRandomPeople(calc)
        calc = ensurePeopleLimits(calc)

        log.trace {
            "People calc: $calc (people=${Model.people}, food=${Model.food}, " +
                "reproRate=${Model.global.reproductionRate}, cap=${Model.totalPeopleCapacity})"
        }
        return calc
    }

    private fun addOrKillRandomPeople(calc: Amount): Amount {
        var result = calc
        // if only a few, add some random people
        if (Model.people < 20 && Model.food.isNotZero && random.nextDouble(0.0, 1.0) < 0.3) {
            val randomAdded = random.nextLong(2, 5)
            log.trace { "people randomly added: $randomAdded" }
            result += randomAdded
        }

        // if more than a few, kill some random people
        if (Model.people > 10 && random.nextDouble(0.0, 1.0) < 0.1) {
            val randomKills = random.nextLong(2, 6)
            log.trace { "people randomly killed: $randomKills" }
            result -= randomKills
        }
        return result
    }

    private fun ensurePeopleLimits(calc: Amount): Amount {
        var result = calc
        if (result + Model.people > Model.totalPeopleCapacity) {
            // was already more people stored than capacity allowed => degrade!
            if (Model.people > Model.totalPeopleCapacity) {
                val overCapacityKill = Amount(-((Model.totalPeopleCapacity.real / 100.0 * 20).toLong()))
                log.trace { "People dying because of over capacity: $overCapacityKill" }
                if (calc < 0) {
                    result += overCapacityKill
                } else {
                    result = overCapacityKill
                }
            } else {
                // do not produce more than capacity allows
                result = Model.totalPeopleCapacity - Model.people
            }
        }

        // amount of people can not be negative
        if (Model.people + result < 0) {
            result = -Model.people
        }
        return result
    }

    private fun calcGold(): Amount {
        var calc = Model.people * Model.global.peopleGoldRate
        calc *= random.nextDouble(0.8, 1.6)
        log.trace { "Gold calc: $calc (people=${Model.people} * rate=${Model.global.peopleGoldRate})" }
        return calc
    }

}
