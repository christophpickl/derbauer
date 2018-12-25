package com.github.christophpickl.derbauer2.ui

import mu.KotlinLogging
import java.awt.Font
import javax.swing.JOptionPane

val VIEW_SIZE = 110 to 30

val DEFAULT_FONT = Font("Monaco", Font.BOLD, 20)

class SwingExceptionHandler : Thread.UncaughtExceptionHandler {
    private val log = KotlinLogging.logger {}
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        log.error(throwable) { "Unhandled exception in: $thread" }
        JOptionPane.showMessageDialog(null, "${throwable.javaClass.simpleName}: ${throwable.message}", "Unexpected Error", JOptionPane.ERROR_MESSAGE)
        System.exit(1)
    }
}
