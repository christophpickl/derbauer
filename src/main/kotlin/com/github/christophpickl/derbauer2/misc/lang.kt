package com.github.christophpickl.derbauer2.misc

import mu.KotlinLogging.logger
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties

private val log = logger {}

inline fun <reified C : Any, reified T> propertiesOfType(thiz: C): List<T> =
    C::class.memberProperties
        .filter { it.returnType.isSubtypeOf(T::class.createType()) }
        .map { it.get(thiz) as T }

interface Listener {}

class Subscription<L : Listener> {

    private val listeners = mutableListOf<L>()

    fun add(listener: L) {
        listeners += listener
    }

    fun broadcast(action: L.() -> Unit) {
        listeners.forEach {
            action(it)
        }
    }
}

@Suppress("unused")
fun Any?.enforceWhenBranches() {
}

fun sleep(ms: Int) {
    Thread.sleep(ms.toLong())
}

object KMath {

    fun minButNotNegative(first: Int, second: Int, vararg others: Int): Int =
        Math.max(0, min(first, second, *others))

    fun min(first: Int, second: Int, vararg others: Int): Int =
        mutableListOf(first, second).apply { addAll(others.toList()) }.min()!!

}

fun execute(vararg commands: String): Int {
    log.info { "Starting process: ${commands.joinToString(" ")}" }
    val builder = ProcessBuilder().apply {
        command(commands.toList())
        redirectOutput(ProcessBuilder.Redirect.INHERIT)
        redirectError(ProcessBuilder.Redirect.INHERIT)
    }
    val process = builder.start()
    val returnCode = process.waitFor()
    if (returnCode != 0) {
        log.warn { "Process returned code ${returnCode} for command: ${commands.joinToString(" ")}" }
    }
    return returnCode
}
