package com.github.christophpickl.derbauer

import ch.qos.logback.classic.Level
import com.github.christophpickl.derbauer.view.DerBauerFxApp
import com.github.christophpickl.kpotpourri.logback4k.Logback4k
import javafx.application.Application

object DerBauer {
    @JvmStatic
    fun main(args: Array<String>) {
        initLogging()
        Application.launch(DerBauerFxApp::class.java, *args)
    }

    private fun initLogging() {
        Logback4k.reconfigure {
            rootLevel = Level.WARN
            packageLevel(Level.ALL, "com.github.christophpickl.derbauer")
            addConsoleAppender {
                pattern = "%d{HH:mm:ss.SS} [%highlight(%-5level)] [%-25thread] %cyan(%logger{40}) - %msg%n"
            }
        }
    }
}
