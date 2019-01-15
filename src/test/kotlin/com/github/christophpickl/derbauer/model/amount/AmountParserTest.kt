package com.github.christophpickl.derbauer.model.amount

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test
class AmountParserTest {

    @DataProvider
    fun invalidTexts() = listOf(
        "", ".", "x",
        "1K", "1.1",
        "1.", "1.k", ".k", ".1k", "1..1k", "1.1.k"
    ).map { arrayOf(it) }.toTypedArray()

    @Test(dataProvider = "invalidTexts")
    fun `When parse invalid text Then return null`(invalidText: String) {
        assertThat(Amount.parse(invalidText)).isNull()
    }

    @DataProvider
    fun validTexts() = listOf(
        "0" to 0L,
        "1" to 1L,
        "1k" to 1_000L,
        "1.1k" to 1_100L,
        "1.01k" to 1_010L,
        "1.001k" to 1_001L,
        "1.0001k" to 1_000L,
        "1m" to 1_000_000L,
        "1.0001m" to 1_000_100L,
        "1.00001m" to 1_000_010L,
        "1.000001m" to 1_000_001L,
        "1.0000001m" to 1_000_000L
    ).map { arrayOf(it.first, it.second) }.toTypedArray()

    @Test(dataProvider = "validTexts")
    fun `When parse valid text Then return proper amount`(text: String, expectedAmount: Long) {
        assertThat(Amount.parse(text)).isEqualTo(Amount(expectedAmount))
    }

}
