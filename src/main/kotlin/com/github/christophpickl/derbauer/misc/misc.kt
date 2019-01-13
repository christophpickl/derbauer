package com.github.christophpickl.derbauer.misc

import kotlin.reflect.KClass

@Suppress("unused")
fun Any?.enforceWhenBranches() {
}

open class DerBauerException(
    message: String,
    cause: Exception? = null
) : RuntimeException(message, cause)

fun restrictMethodUsage(
    basePackageName: String,
    requiredAnnotation: KClass<out Annotation>,
    skipClasses: Set<String> = setOf("mu.internal.LocationAwareKLogger")
) {
    val stackTraceElements = Thread.currentThread().stackTrace.toList()

    val hasSomeoneRequiredAnnotation = stackTraceElements
        .filter { it.className.startsWith(basePackageName) }
        .any { stack ->
            val method = Class.forName(stack.className).declaredMethods.first { it.name == stack.methodName }
            method.getAnnotation(requiredAnnotation.java) != null
        }

    val skip = if (skipClasses.isEmpty()) false else stackTraceElements.any { skipClasses.contains(it.className) }

    if (!hasSomeoneRequiredAnnotation && !skip) {
        throw UnsupportedOperationException(
            "Method must be annotated with @${requiredAnnotation.simpleName} in order to use it! " +
                // 0 ... Thread.getStackTrace
                // 1 ... restricted method
                // 2 ... invoking method
                "Invoking method was: ${stackTraceElements[2].toLabel()}")
    }
}
