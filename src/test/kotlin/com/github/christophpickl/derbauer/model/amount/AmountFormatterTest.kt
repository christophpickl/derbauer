package com.github.christophpickl.derbauer.model.amount

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test
class AmountFormatterTest {

    // @formatter:off
    @DataProvider
    fun amountFormatted() = arrayOf(
        arrayOf(                  0L,   "0"),
        arrayOf(                  1L,   "1"),
        arrayOf(                999L, "999"),
        arrayOf(              1_000L,   "1k"),
        arrayOf(              1_020L,   "1.02k"),
        arrayOf(              1_200L,   "1.2k"),
        arrayOf(              1_239L,   "1.23k"),
        arrayOf(             12_000L,  "12k"),
        arrayOf(             12_399L,  "12.3k"),
        arrayOf(            999_999L, "999k"),
        arrayOf(          1_020_000L,   "1.02m"),
        arrayOf(          1_200_000L,   "1.2m"),
        arrayOf(        999_999_999L, "999m"),
        arrayOf(      1_000_000_000L,   "1g"),
        arrayOf(    999_999_999_999L, "999g"),
        arrayOf(  1_000_000_000_000L,   "1t"),
        arrayOf(999_999_999_999_999L, "999t")
    )
    // @formatter:on

    @Test(dataProvider = "amountFormatted")
    fun `Amount formatted`(realAmount: Long, expectedFormatted: String) {
        assertThat(Amount(realAmount).formatted).isEqualTo(expectedFormatted)
    }

    @Test(dataProvider = "amountFormatted")
    fun `Amount formatted negative`(realAmount: Long, expectedFormatted: String) {
        assertThat(Amount(-realAmount).formatted).isEqualTo(if (realAmount == 0L) "0" else "-$expectedFormatted")
    }

}
