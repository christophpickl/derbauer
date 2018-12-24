package com.github.christophpickl.derbauer2.misc

fun <E : Ordered> List<E>.ordered(): List<E> =
    sortedBy { it.order }

interface Ordered {
    val order: Int
}
