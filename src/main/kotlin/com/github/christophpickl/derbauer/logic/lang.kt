package com.github.christophpickl.derbauer.logic

import kotlin.reflect.KMutableProperty1

fun <T> KMutableProperty1<T, Int>.increment(instance: T, amount: Int) {
    val oldValue = get(instance)
    set(instance, oldValue + amount)
}

fun <T> KMutableProperty1<T, Int>.decrement(instance: T, amount: Int) {
    val oldValue = get(instance)
    set(instance, oldValue - amount)
}
