package com.github.christophpickl.derbauer.model

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.nullify
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.DataProvider
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class PlayerTest {

    // @formatter:off
    @DataProvider
    fun wealthProvider() = arrayOf(
        arrayOf( 123, 0.1,  10),
        arrayOf(1999, 0.3, 300),
        arrayOf(-100, 0.1,   0),
        arrayOf(   1, 0.1,   0)
    )
    // @formatter:on

    @Test(dataProvider = "wealthProvider")
    fun `Given predefined wealth When get relative wealth Then return proper amount`(
        gold: Long,
        wealthPercentage: Double,
        expectedWealth: Long
    ) {
        Model.nullify()
        Model.gold = Amount(gold)

        val relativeWealth = Model.player.relativeWealthBy(wealthPercentage)

        assertThat(relativeWealth.real).isEqualTo(expectedWealth)
    }

}
