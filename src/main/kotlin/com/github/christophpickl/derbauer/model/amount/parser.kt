package com.github.christophpickl.derbauer.model.amount


interface AmountParser {
    fun parse(text: String): Amount?
}

object AmountParserImpl : AmountParser {
    override fun parse(text: String): Amount? {
        text.toLongOrNull()?.let {
            return Amount(it)
        }
        return AmountType.valuesButSingle.mapNotNull { type ->
            type.regexp.matchEntire(text)?.let {
                type to it
            }
        }.firstOrNull()?.let { (type, match) ->
            val floatString = match.groupValues[2]
            val floatDouble = if (floatString.isEmpty()) {
                0.0
            } else { // floatString = ".12"
                "0$floatString".toDouble()
            }
            val properFloat = (floatDouble * type.thousands).toLong()
            Amount(match.groupValues[1].toLong() * type.thousands + properFloat)
        }
    }

}
