package com.github.christophpickl.derbauer2.misc

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.WindowConstants

class MainFrame : JFrame() {

    private val backgroundColor = Color(194, 177, 55)

    init {
        title = "Der Bauer 2"
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }

    fun buildAndShow(content: Component) {
        contentPane.add(buildRoot(content))
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
}


class MainTextArea() : JTextArea() {
    init {
        isEditable = false
        isOpaque = false
        rows = 20
        columns = 60
        font = Font("Monaco", Font.BOLD, 14)
    }
}
