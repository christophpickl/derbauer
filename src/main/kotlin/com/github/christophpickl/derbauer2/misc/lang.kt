package com.github.christophpickl.derbauer2.misc

import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties

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

fun <T, R> KProperty1<T, R>.isNotPrivateFinal() =
    !isFinal && this.visibility != KVisibility.PRIVATE
