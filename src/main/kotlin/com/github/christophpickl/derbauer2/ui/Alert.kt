package com.github.christophpickl.derbauer2.ui

import mu.KotlinLogging.logger
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Toolkit
import java.util.*
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel

object Alert {

    private val log = logger {}
    lateinit var glassPane: JPanel
    private var currentTimer: Timer? = null
    private val label = JLabel("").apply {
        font = DEFAULT_FONT
    }

    val panel = JPanel().apply {
        isOpaque = false
        val container = JPanel()
        container.add(label)
        container.background = Color(80, 190, 90, 70)
        container.border = BorderFactory.createEmptyBorder(30, 0, 0, 0)
        layout = BorderLayout()
        val vgap = 360
        add(vgap(vgap), BorderLayout.NORTH)
        add(container, BorderLayout.CENTER)
        add(vgap(vgap), BorderLayout.SOUTH)
    }

    fun show(type: AlertType) {
        log.debug { "show($type)" }
        label.text = type.message
        glassPane.isVisible = true
        beep()
        resetTimer()
    }

    private fun vgap(height: Int) = JPanel().apply {
        isOpaque = false
        preferredSize = Dimension(size.width, height)
    }

    private fun resetTimer() {
        currentTimer?.cancel()
        currentTimer = Timer().apply {
            schedule(makeGlassPaneInvisibleTask(), 2_000)
        }
    }

    private fun makeGlassPaneInvisibleTask() = object : TimerTask() {
        override fun run() {
            glassPane.isVisible = false
        }
    }

}

sealed class AlertType(val message: String) {
    object NotEnoughGold : AlertType("Not enough gold!")
    object NotEnoughCapacity : AlertType("Not enough capacity!")
    object NotEnoughUnused : AlertType("Not enough unused!")
    object NotEnoughLand : AlertType("Not enough land!")
    object NotEnoughResourcesToSell : AlertType("Not enough resources to sell!")
//    class CustomAlert(message: String) : AlertType(message)
}

fun beep() {
    Toolkit.getDefaultToolkit().beep()
    println("\uD83D\uDD14\uD83D\uDD14\uD83D\uDD14")
}

//fun <T> beepReturn(): T? {
//    beep()
//    return null
//}
