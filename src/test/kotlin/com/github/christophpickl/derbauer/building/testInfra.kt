package com.github.christophpickl.derbauer.building

import com.github.christophpickl.derbauer.model.Amount

fun Buildings.foodProducerAmountToZero() {
    all.filterIsInstance<FoodProducingBuilding>().forEach { it.amount = Amount.zero }
}
