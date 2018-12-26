package com.github.christophpickl.derbauer2

import ch.qos.logback.classic.Level
import com.github.christophpickl.derbauer2.home.HomeView
import com.github.christophpickl.derbauer2.misc.Debugger
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.Keyboard
import com.github.christophpickl.derbauer2.ui.MainFrame
import com.github.christophpickl.derbauer2.ui.MainTextArea
import com.github.christophpickl.derbauer2.ui.Prompt
import com.github.christophpickl.derbauer2.ui.RendererImpl
import com.github.christophpickl.kpotpourri.logback4k.Logback4k
import com.github.christophpickl.kpotpourri.swing.AbortingExceptionHandler
import javax.swing.SwingUtilities

val CHEAT_MODE = System.getProperty("derbauer2.cheatmode") != null

object DerBauer2 {
    @JvmStatic
    fun main(args: Array<String>) {
        initLogging()
        val exceptionHandler = AbortingExceptionHandler()
        try {
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
        } catch(e : Exception) {
            exceptionHandler.uncaughtException(Thread.currentThread(), e)
        }
    }

    private fun initLogging() {
        Logback4k.reconfigure {
            rootLevel = Level.WARN
            packageLevel(Level.ALL, "com.github.christophpickl.derbauer2")
            addConsoleAppender {
                pattern = "%d{HH:mm:ss} [%gray(%thread)] [%highlight(%-5level)] %cyan(%logger{30}) - %msg%n"
            }
        }
    }
}
