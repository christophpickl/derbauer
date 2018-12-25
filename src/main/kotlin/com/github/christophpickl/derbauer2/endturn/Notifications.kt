package com.github.christophpickl.derbauer2.endturn

class Notifications {

    private val messages = mutableListOf<String>()

    fun add(message: String) {
        messages += message
    }

    fun consumeAll(): List<String> {
        val copy = ArrayList(messages)
        messages.clear()
        return copy
    }

}
