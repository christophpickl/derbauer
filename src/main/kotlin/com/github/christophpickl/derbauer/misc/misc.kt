package com.github.christophpickl.derbauer.misc

@Suppress("unused")
fun Any?.enforceWhenBranches() {
}

open class DerBauerException(
    message: String,
    cause: Exception? = null
) : RuntimeException(message, cause)
