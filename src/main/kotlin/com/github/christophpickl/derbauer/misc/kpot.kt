package com.github.christophpickl.derbauer.misc

fun <T> infiniteListOf(vararg elements: T) = InfiniteList(elements.toList())

data class InfiniteList<T>(
    val elements: List<T>
) : List<T> by elements {

    init {
        require(elements.isNotEmpty()) { "Given elements must not be empty." }
    }

    private var iterator = elements.iterator()

    fun nextInfinite(): T {
        if (!iterator.hasNext()) {
            iterator = elements.iterator()
        }
        return iterator.next()
    }

}
