package com.github.christophpickl.derbauer2

import ch.qos.logback.classic.Level
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.Keyboard
import com.github.christophpickl.derbauer2.ui.MainFrame
import com.github.christophpickl.derbauer2.ui.MainTextArea
import com.github.christophpickl.derbauer2.ui.Prompt
import com.github.christophpickl.derbauer2.ui.Renderer
import com.github.christophpickl.kpotpourri.logback4k.Logback4k
import javax.swing.SwingUtilities

val CHEAT_MODE = System.getProperty("not_existing") == null

object DerBauer2 {
    @JvmStatic
    fun main(args: Array<String>) {
        initLogging()
        Model.reset()

        val prompt = Prompt()
        val text = MainTextArea()
        val keyboard = Keyboard()

        val renderer = Renderer(text, prompt)
        val engine = Router(renderer)

        text.addKeyListener(keyboard)
        keyboard.subscription.add(prompt)
        prompt.subscription.add(engine)

        renderer.render()
        SwingUtilities.invokeLater {
            MainFrame().buildAndShow(text)
        }
    }

    private fun initLogging() {
        Logback4k.reconfigure {
            rootLevel = Level.WARN
            packageLevel(Level.ALL, "com.github.christophpickl.derbauer2")
            addConsoleAppender {
                pattern = "%d{HH:mm:ss} [%highlight(%-5level)] %cyan(%logger{30}) - %msg%n"
            }
        }
    }
}
