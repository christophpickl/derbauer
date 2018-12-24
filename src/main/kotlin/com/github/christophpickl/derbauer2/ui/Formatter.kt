package com.github.christophpickl.derbauer2.ui

import com.github.christophpickl.kpotpourri.common.string.times

object Formatter {

    fun formatNumber(number: Int, length: Int, addPlusSign: Boolean = false): String {
        val realLength = if (addPlusSign) length - 1 else length
        val prefix = if (addPlusSign && number > 0) "+" else ""
        return String.format("%$prefix${realLength}d", number)
    }

    fun formatRightBound(string: String, length: Int): String {
        if (string.length >= length) return string
        return " ".times(length - string.length) + string
    }

}
