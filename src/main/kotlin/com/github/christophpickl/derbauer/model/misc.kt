package com.github.christophpickl.derbauer.model

import com.github.christophpickl.derbauer.logic.formatNumber

object ResourceFormats {
    const val foodDigits = 4
    const val goldDigits = 4
    const val landDigits = 4
    const val peopleDigits = 3
    fun formatFood(food: Int) = formatNumber(food, foodDigits)
    fun formatGold(gold: Int) = formatNumber(gold, goldDigits)
    fun formatLand(land: Int) = formatNumber(land, landDigits)
    fun formatPeople(people: Int) = formatNumber(people, peopleDigits)
}
