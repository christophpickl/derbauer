package com.github.christophpickl.derbauer.misc

@Suppress("unused")
fun Any?.enforceWhenBranches() {
}

fun StackTraceElement.toLabel() = "$className#$methodName():$lineNumber"

open class DerBauerException(
    message: String,
    cause: Exception? = null
) : RuntimeException(message, cause)
