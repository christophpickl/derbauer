package com.github.christophpickl.derbauer2.misc

import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties

inline fun <reified C : Any, reified T> propertiesOfType(thiz: C): List<T> =
    C::class.memberProperties
        .filter { it.returnType.isSubtypeOf(T::class.createType()) }
        .map { it.get(thiz) as T }
