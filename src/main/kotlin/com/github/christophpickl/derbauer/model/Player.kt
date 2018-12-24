package com.github.christophpickl.derbauer.model

import com.github.christophpickl.derbauer.army.Armies
import com.github.christophpickl.derbauer.build.Buildings

@Deprecated(message = "v2")
class Player {
    var gold = 0
    val goldFormatted get() = ResourceFormats.formatGold(gold)
    val goldAll get() = Triple(gold, goldFormatted, ResourceFormats.goldDigits)

    var land = 0
    val landFormatted get() = ResourceFormats.formatLand(land)
    val landAll get() = Triple(land, landFormatted, ResourceFormats.landDigits)

    var food = 0
    val foodFormatted get() = ResourceFormats.formatFood(food)
    val foodAll get() = Triple(food, foodFormatted, ResourceFormats.foodDigits)

    var people = 0
    val peopleFormatted get() = ResourceFormats.formatPeople(people)
    val peopleAll get() = Triple(people, peopleFormatted, ResourceFormats.peopleDigits)

    val buildings = Buildings()
    val armies = Armies()

    val landAvailable get() = land - buildings.totalCount

    fun reset() {
        gold = if (DEPRECATED_CHEAT_MODE) 500 else 100
        land = if (DEPRECATED_CHEAT_MODE) 100 else 5
        food = if (DEPRECATED_CHEAT_MODE) 800 else 300
        people = if (DEPRECATED_CHEAT_MODE) 9 else 2

        buildings.reset()
        armies.reset()
    }

    override fun toString() = "Player{gold=$gold, land=$land, food=$food, people=$people, buildings=$buildings, armies=$armies}"
}
