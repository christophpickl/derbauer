package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.kpotpourri.common.string.times

data class Size(
    val width: Int,
    val height: Int
)

fun beep() {
    java.awt.Toolkit.getDefaultToolkit().beep()
    println("\uD83D\uDD14\uD83D\uDD14\uD83D\uDD14")
}

fun <T> beepReturn(): T? {
    beep()
    return null
}

fun formatRightBound(string: String, length: Int): String {
    if (string.length >= length) return string
    return " ".times(length - string.length) + string
}
fun formatNumber(number: Int, length: Int, addPlusSign: Boolean = false): String {
    val realLength = if (addPlusSign) length - 1 else length
    val prefix = if (addPlusSign && number > 0) "+" else ""
    return String.format("%$prefix${realLength}d", number)
}
