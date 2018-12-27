package com.github.christophpickl.derbauer.build

fun Buildings.foodProducerAmountToZero() {
    all.filterIsInstance<FoodProducingBuilding>().forEach { it.amount = 0 }
}
