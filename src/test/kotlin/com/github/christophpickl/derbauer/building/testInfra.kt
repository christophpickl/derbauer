package com.github.christophpickl.derbauer.building

import com.github.christophpickl.derbauer.model.Amount

fun Buildings.foodProducerAmountToZero() {
    filterAll<FoodProducingBuilding>().forEach { it.amount = Amount.zero }
}

fun Buildings.foodCapacityAmountToZero() {
    filterAll<FoodCapacityBuilding>().forEach { it.amount = Amount.zero }
}

fun Buildings.peopleCapacityAmountToZero() {
    filterAll<PeopleCapacityBuilding>().forEach { it.amount = Amount.zero }
}
