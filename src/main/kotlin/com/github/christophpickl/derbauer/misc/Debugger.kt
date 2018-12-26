package com.github.christophpickl.derbauer.misc

import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.process.ProcessExecuterImpl
import java.awt.BorderLayout
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.io.File
import java.nio.file.Files
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.text.DefaultCaret

fun main(args: Array<String>) {
    DebugWindow().isVisible = true
}

object Debugger {

    private var window: DebugWindow? = null

    fun asKeyListener() = object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
            if (e.keyCode == KeyEvent.VK_D && e.isControlDown) {
                showWindow()
            }
        }
    }

    private fun showWindow() {
        if (window == null) {
            window = DebugWindow()
        }
        window!!.isVisible = true
    }
}

class DebugWindow : JDialog() {

    private val text = JTextArea().apply {
        isEditable = false
        (caret as DefaultCaret).updatePolicy = DefaultCaret.NEVER_UPDATE
    }
    private val scrollPane = JScrollPane(text)

    init {
        title = "Debugger"
        contentPane.add(rootPanel())
        setSize(500, 800)
    }

    private fun rootPanel() =
        JPanel().apply {
            layout = BorderLayout()
            add(scrollPane, BorderLayout.CENTER)
            add(JPanel().apply {
                add(JButton("Refresh").apply {
                    addActionListener { onRefresh() }
                })
                add(JButton("Open").apply {
                    addActionListener { open() }
                })
            }, BorderLayout.SOUTH)
            onRefresh()
        }

    private fun onRefresh() {
        text.text = Model.toJson()
    }

    private fun open() {
        val text = Model.toJson()
        val file = Files.createTempFile("derbauer_model_dump_", ".json").toFile()
        file.writeText(text)
        ProcessExecuterImpl.execute("open", listOf(file.absolutePath), cwd = File("."))
    }

}
