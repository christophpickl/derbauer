package com.github.christophpickl.derbauer2.build

fun Buildings._foodProducerAmountToZero() {
    all.filterIsInstance<FoodProducingBuilding>().forEach { it.amount = 0 }
}
