package com.github.christophpickl.derbauer.view

import javafx.scene.input.KeyEvent
import mu.KotlinLogging.logger

class Keyboard {

    private val log = logger {}

    fun onReleased(event: KeyEvent) {
        log.debug { "onReleased(event.code=${event.code})" }
    }

}
