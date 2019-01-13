package com.github.christophpickl.derbauer.model.amount

interface AmountRounder {
    fun round(type: AmountType, real: Long): Long
}

object AmountRounderImpl : AmountRounder {

    override fun round(type: AmountType, real: Long): Long =
        if (type == AmountType.Single) real
        else {
            val rest = real % type.thousands
            val rawRounded = real - rest
            val addition = computeRoundAddition(type, real, rest, rawRounded)
            rawRounded + (addition * (if (real < 0) -1 else 1))
        }

    private fun computeRoundAddition(type: AmountType, real: Long, rest: Long, rawRounded: Long): Long {
        val absRealCut = Math.abs(rawRounded / type.thousands)
        val absRest = Math.abs(rest)
        return when {
            absRealCut < 10 -> computeRound2Additions(type, real, rest, absRest)
            absRealCut < 100 -> computeRound1Additions(type, absRest)
            else -> 0
        }
    }

    private fun computeRound2Additions(type: AmountType, real: Long, rest: Long, absRest: Long): Long =
        if (rest == 0L) 0 else {
            absRest.toString().let {
                if (Math.abs(real).toString().elementAt(1) == '0') "0$it" else it
            }.substring(0, 2).toLong() * 10 * type.thousandsForNext
        }

    private fun computeRound1Additions(type: AmountType, absRest: Long): Long =
        absRest.toString().substring(0, 1).toInt() * 100 * type.thousandsForNext

}
