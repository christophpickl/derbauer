package com.github.christophpickl.derbauer.misc

import com.github.christophpickl.derbauer.misc.KRand.gaussLimitted
import java.util.*

fun main(args: Array<String>) {
    (1..100).map { _ ->
        gaussLimitted(50.0, 20.0)
    }.sorted()
        .forEach { println(it) }
//    println(f.first())
//    println(f.last())
}

// TODO improve and use ;)
object KRand {

    private val random = Random()

    fun gauss(mean: Double, deviation: Double): Double {
        return random.nextGaussian() * deviation + mean
    }

    fun gaussLimitted(mean: Double, minMaxDeviation: Double): Double {
        var tmp: Double
        val lower = mean - minMaxDeviation
        val upper = mean + minMaxDeviation
        do {
            tmp = gauss(mean, minMaxDeviation / 3)
        } while (tmp < lower || tmp > upper)
        return tmp
    }
}
