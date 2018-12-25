package com.github.christophpickl.derbauer2.misc

import java.util.*
import kotlin.random.Random

object Rand {

    fun randomize(int: Int, from: Double, to: Double) = (int * Random.nextDouble(from, to)).toInt()

}

fun <E> randomListOf(first: Pair<E, Int>, vararg elements: Pair<E, Int>): RandomList<E> =
    RandomListImpl(mutableListOf(first).apply { addAll(elements.toList()) })

interface RandomList<E> : List<E> {
    fun randomElement(): E
}

private class RandomListImpl<E>(
    private val elements: List<Pair<E, Int>>
) : ArrayList<E>(elements.map { it.first }), RandomList<E> {

    init {
        require(elements.sumBy { it.second } == 100) {
            "Percentage must sum up to 100%, but was: ${elements.sumBy { it.second }}%"
        }
    }

    override fun randomElement(): E {
        val rand = Random.nextInt(0, 100)
        var skipWindow = 0
        elements.forEach { element ->
            val window = skipWindow..(skipWindow + element.second - 1)
            if (window.contains(rand)) {
                return element.first
            }
            skipWindow += element.second
        }
        throw IllegalStateException("Internal error. No random element could be calculated!")
    }
}
