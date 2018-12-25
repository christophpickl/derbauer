package com.github.christophpickl.derbauer2.misc

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.christophpickl.derbauer2.model.Model
import java.awt.BorderLayout
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
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
    private val mapper = jacksonObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)

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
        text.text = mapper.writeValueAsString(Model)
    }

    private fun open() {
        val text = mapper.writeValueAsString(Model)
        val file = Files.createTempFile("derbauer2_model_dump_", ".json").toFile()
        file.writeText(text)
        execute("open", file.absolutePath)
    }

}
