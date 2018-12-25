package com.github.christophpickl.derbauer2.ui

import mu.KotlinLogging.logger
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.WindowConstants

class MainFrame : JFrame() {

    private val backgroundColor = Color(194, 177, 55)
    private val log = logger {}

    init {
        title = "Der Bauer II"
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }

    fun buildAndShow(content: Component) {
        log.debug { "Showing main frame." }
        contentPane.add(buildRoot(content))
        prepareGlassPane()
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }

    private fun buildRoot(content: Component): Component =
        JPanel(BorderLayout()).apply {
            background = backgroundColor
            border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
            add(content, BorderLayout.CENTER)
        }

    private fun prepareGlassPane() {
        val glass = glassPane as JPanel
        glass.isVisible = false
        Alert.glassPane = glass
        glass.layout = BorderLayout()
        glass.add(Alert.panel, BorderLayout.CENTER)
    }
}

class MainTextArea() : JTextArea() {
    init {
        isEditable = false
        isOpaque = false
        columns = VIEW_SIZE.first
        rows = VIEW_SIZE.second
        font = DEFAULT_FONT
    }
}
