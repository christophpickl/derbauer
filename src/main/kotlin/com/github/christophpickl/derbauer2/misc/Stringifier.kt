package com.github.christophpickl.derbauer2.misc

import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

@Target(AnnotationTarget.PROPERTY)
annotation class IgnoreStringified

object Stringifier {
    // MINOR find annotation also on superclass
    inline fun <reified T : Any> stringify(any: T) = "${T::class.simpleName}{${
    T::class.memberProperties.filter { it.findAnnotation<IgnoreStringified>() == null && it.isNotPrivateFinal() }.joinToString() {
        "${it.name}=${it.get(any)}"
    }}"

}
