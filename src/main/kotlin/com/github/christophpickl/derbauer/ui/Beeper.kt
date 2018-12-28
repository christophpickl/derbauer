package com.github.christophpickl.derbauer.ui

import mu.KotlinLogging
import java.awt.Toolkit

interface Beeper {
    fun beep(reason: String)
}

object RealBeeper : Beeper {
    private val log = KotlinLogging.logger {}

    override fun beep(reason: String) {
        log.debug { "Beep reason: $reason" }
        Toolkit.getDefaultToolkit().beep()
        println("\uD83D\uDD14\uD83D\uDD14\uD83D\uDD14")
    }

    //fun <T> beepReturn(): T? {
    //    beep()
    //    return null
    //}

}
