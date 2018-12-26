package com.github.christophpickl.derbauer.build

fun Buildings._foodProducerAmountToZero() {
    all.filterIsInstance<FoodProducingBuilding>().forEach { it.amount = 0 }
}