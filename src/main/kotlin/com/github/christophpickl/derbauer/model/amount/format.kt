package com.github.christophpickl.derbauer.model.amount

interface AmountFormatter {
    fun format(real: Long, type: AmountType, rounded: Long): String
}

object AmountFormatterImpl : AmountFormatter {

    override fun format(real: Long, type: AmountType, rounded: Long): String =
        if (type == AmountType.Single) {
            rounded.toString()
        } else {
            val realCut = rounded / type.thousands
            val absRealCut = Math.abs(realCut)
            val absReal = Math.abs(real)

            val floatPart = when {
                absRealCut < 10 -> {
                    // real=1239; rounded=1230, realCut=1 ===> 1239.substring=239, take2=23 => 1.23
                    ".${absReal.toString().substring(1).take(2)}"
                }
                absRealCut < 100 -> {
                    // real=12399; realCut=12 ===> realCut.length=2, real.sub(2)=399, first=3 => 12.3
                    ".${absReal.toString().substring(2).take(1)}"
                }
                else -> ""
            }.trimEnd('0').let { if (it == ".") "" else it }
            "$realCut$floatPart${type.sign}"
        }

}
