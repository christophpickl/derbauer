package com.github.christophpickl.derbauer2.military

import mu.KotlinLogging.logger

class MilitaryController : MilitaryCallback {

    private val log = logger {}

    override fun onMilitary(choice: MilitaryChoice) {
        log.debug { "military action: $choice" }
        // FIXME implement military action
    }

}
