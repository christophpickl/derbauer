package com.github.christophpickl.derbauer2.misc

import kotlin.random.Random

object Rand {

    fun rand0to1() = Random.nextDouble(0.0, 1.0)

    fun randomize(int: Int, from: Double, to: Double) = (int * Random.nextDouble(from, to)).toInt()
}
