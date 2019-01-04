package com.github.christophpickl.derbauer.model

import com.github.christophpickl.derbauer.DEV_MODE_PROPERTY
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test
class AmountTest {

    @BeforeClass
    fun `init system property`() {
        System.setProperty(DEV_MODE_PROPERTY, "1")
    }

    @AfterClass
    fun `reset system property`() {
        System.clearProperty(DEV_MODE_PROPERTY)
    }

    fun `Given non-annotated method When invoking toString Then throw`() {
        Assertions.assertThatThrownBy {
            Amount(1).toString()
        }.isExactlyInstanceOf(UnsupportedOperationException::class.java)
    }

    @AmountToStringAllowed
    fun `Given annotated method When invoking toString Then dont throw`() {
        Amount(1).toString()
    }

    // @formatter:off
    @DataProvider
    fun amountTypes() = arrayOf(
        arrayOf(                    0L, AmountType.Single),
        arrayOf(                1_000L, AmountType.Kilo),
        arrayOf(            1_000_000L, AmountType.Mega),
        arrayOf(        1_000_000_000L, AmountType.Giga),
        arrayOf(    1_000_000_000_000L, AmountType.Tera),
        arrayOf(1_000_000_000_000_000L, AmountType.Tera)
    )
    // @formatter:on

    @Test(dataProvider = "amountTypes")
    fun `Amount types`(realAmount: Long, expectedType: AmountType) {
        assertThat(Amount(realAmount).type).isEqualTo(expectedType)
    }

    @Test(dataProvider = "amountTypes")
    fun `Amount types negative`(realAmount: Long, expectedType: AmountType) {
        assertThat(Amount(-realAmount).type).isEqualTo(expectedType)
    }

    // @formatter:off
    @DataProvider
    fun amountRounded() = arrayOf(
        arrayOf(        0L,           0L),
        arrayOf(      999L,         999L),
        arrayOf(    1_000L,       1_000L),
        arrayOf(    1_123L,       1_000L),
        arrayOf(1_123_456L,   1_000_000L)
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

    // @formatter:off
    @DataProvider
    fun amountFormatted() = arrayOf(
        arrayOf(                  0L,   "0"),
        arrayOf(                  1L,   "1"),
        arrayOf(                999L, "999"),
        arrayOf(              1_000L,   "1k"),
        arrayOf(            999_999L, "999k"),
        arrayOf(          1_000_000L,   "1m"),
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

    fun `AmountType proper thousands and limits`() {
        assertThat(AmountType.Single.thousands).isEqualTo(0L)
        assertThat(AmountType.Single.limit).isEqualTo(1_000L)

        assertThat(AmountType.Kilo.thousands).isEqualTo(1_000L)
        assertThat(AmountType.Kilo.limit).isEqualTo(1_000_000L)

        assertThat(AmountType.Mega.thousands).isEqualTo(1_000_000L)
        assertThat(AmountType.Mega.limit).isEqualTo(1_000_000_000L)

        assertThat(AmountType.Giga.thousands).isEqualTo(1_000_000_000L)
        assertThat(AmountType.Giga.limit).isEqualTo(1_000_000_000_000L)

        assertThat(AmountType.Tera.thousands).isEqualTo(1_000_000_000_000L)
        assertThat(AmountType.Tera.limit).isEqualTo(1_000_000_000_000_000L)
    }

}
