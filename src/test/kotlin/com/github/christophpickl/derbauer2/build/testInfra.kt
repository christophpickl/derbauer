package com.github.christophpickl.derbauer2.build

fun Buildings.foodProducerAmountToZero() {
    all.filterIsInstance<FoodProducingBuilding>().forEach { it.amount = 0 }
}
