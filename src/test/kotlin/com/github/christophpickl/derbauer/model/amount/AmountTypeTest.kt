package com.github.christophpickl.derbauer.model.amount

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test
class AmountTypeTest {

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
