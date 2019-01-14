package com.github.christophpickl.derbauer.building

import com.github.christophpickl.derbauer.model.amount.Amount

fun Buildings.foodProducerAmountToZero() {
    filterAll<FoodProducingBuilding>().forEach { it.amount = Amount.zero }
}

fun Buildings.foodCapacityAmountToZero() {
    filterAll<FoodCapacityBuilding>().forEach { it.amount = Amount.zero }
}

fun Buildings.peopleCapacityAmountToZero() {
    filterAll<PeopleCapacityBuilding>().forEach { it.amount = Amount.zero }
}

fun Buildings.allAmountToZero() {
    all.forEach { it.amount = Amount.zero }
}
