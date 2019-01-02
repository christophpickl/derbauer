package com.github.christophpickl.derbauer

import ch.qos.logback.classic.Level
import com.github.christophpickl.derbauer.home.HomeView
import com.github.christophpickl.derbauer.misc.Debugger
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.Keyboard
import com.github.christophpickl.derbauer.ui.MainFrame
import com.github.christophpickl.derbauer.ui.MainTextArea
import com.github.christophpickl.derbauer.ui.Prompt
import com.github.christophpickl.derbauer.ui.RendererImpl
import com.github.christophpickl.kpotpourri.logback4k.Logback4k
import com.github.christophpickl.kpotpourri.swing.AbortingExceptionHandler
import mu.KotlinLogging
import javax.swing.SwingUtilities

val CHEAT_MODE_PROPERTY = "derbauer.cheat"
val DEV_MODE_PROPERTY = "derbauer.dev"
private val log = KotlinLogging.logger {}

val CHEAT_MODE get() = (System.getProperty(CHEAT_MODE_PROPERTY) != null)
val DEV_MODE get() = (System.getProperty(DEV_MODE_PROPERTY) != null)

object DerBauer {

    private val exceptionHandler = AbortingExceptionHandler()
    
    @JvmStatic
    @Suppress("TooGenericExceptionCaught")
    fun main(args: Array<String>) {
        try {
            initLogging()
            startDerBauer()
        } catch (e: Exception) {
            exceptionHandler.uncaughtException(Thread.currentThread(), e)
        }
    }

    private fun startDerBauer() {
        Model.currentView = HomeView()

        val prompt = Prompt()
        val text = MainTextArea()
        val keyboard = Keyboard()

        val renderer = RendererImpl(text, prompt)
        val engine = Router(renderer)

        text.addKeyListener(keyboard)
        text.addKeyListener(Debugger.asKeyListener())
        keyboard.dispatcher.add(prompt)
        prompt.dispatcher.add(engine)

        renderer.render()
        SwingUtilities.invokeLater {
            Thread.currentThread().uncaughtExceptionHandler = exceptionHandler
            MainFrame().buildAndShow(text)
        }
    }

    private fun initLogging() {
        Logback4k.reconfigure {
            rootLevel = Level.WARN
            packageLevel(Level.ALL, "com.github.christophpickl.derbauer")
            addConsoleAppender {
                pattern = "%d{HH:mm:ss} [%gray(%thread)] [%highlight(%-5level)] %cyan(%logger{30}) - %msg%n"
            }
        }

        if (CHEAT_MODE) {
            log.info { "CHEAT MODE enabled" }
        }
        if (DEV_MODE) {
            log.info { "DEV MODE enabled" }
        }
    }
}
