package com.github.christophpickl.derbauer2.misc

open class OncePredicate(
    private val predicate: () -> Boolean
) {

    private var wasEnabled = false

    fun checkAndGet(): OnceResult =
        if (wasEnabled) {
            OnceResult.WasAlready
        } else {
            val checked = predicate()
            if (checked) {
                wasEnabled = true
                OnceResult.ChangedToTrue
            } else {
                OnceResult.Unfilfilled
            }
        }

}

enum class OnceResult {
    Unfilfilled,
    ChangedToTrue,
    WasAlready
}
