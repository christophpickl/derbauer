package com.github.christophpickl.derbauer.model.amount

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test
class AmountRounderTest {

    // @formatter:off
    @DataProvider
    fun amountRounded() = arrayOf(
        arrayOf(          0L,           0L),
        arrayOf(        999L,         999L),
        arrayOf(      1_239L,       1_230L),
        arrayOf(     12_399L,      12_300L),
        arrayOf(  1_234_567L,   1_230_000L),
        arrayOf(  1_012_345L,   1_010_000L),
        arrayOf( 12_345_678L,  12_300_000L),
        arrayOf( 12_000_000L,  12_000_000L)
    )
    // @formatter:on

    @Test(dataProvider = "amountRounded")
    fun `Amount rounded`(realAmount: Long, expectedRounded: Long) {
        assertThat(Amount(realAmount).rounded).isEqualTo(expectedRounded)
    }

    @Test(dataProvider = "amountRounded")
    fun `Amount rounded negative`(realAmount: Long, expectedRounded: Long) {
        assertThat(Amount(-realAmount).rounded).isEqualTo(-expectedRounded)
    }

}
