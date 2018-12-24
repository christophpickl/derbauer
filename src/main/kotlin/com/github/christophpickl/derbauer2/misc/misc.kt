package com.github.christophpickl.derbauer2.misc

import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties


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

@Target(AnnotationTarget.PROPERTY)
annotation class IgnoreStringified

object Stringifier {
    // TODO find annotation also on superclass
    inline fun <reified T : Any> stringify(any: T) = "${T::class.simpleName}{${
    T::class.memberProperties.filter { it.findAnnotation<IgnoreStringified>() == null }.joinToString() {
        "${it.name}=${it.get(any)}"
    }}"
}
