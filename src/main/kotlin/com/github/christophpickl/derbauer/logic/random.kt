package com.github.christophpickl.derbauer.logic

import kotlin.random.Random

fun randomize(int: Int, from: Double, to: Double) = (int * Random.nextDouble(from, to)).toInt()
