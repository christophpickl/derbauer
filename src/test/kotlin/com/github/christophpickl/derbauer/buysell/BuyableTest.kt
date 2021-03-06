package com.github.christophpickl.derbauer.buysell

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class BuyableTest {

    fun `Given not really enough gold When get buy possible amount Then return 1 as rounded price is used`() {
        val buyable = TestBuyable(buyPrice = Amount(1_042))
        Model.gold = Amount(1_040)

        assertThat(buyable.buyPossibleAmount.real).isEqualTo(1)
    }

    fun `Given not really enough gold When get effective buy possible amount Then return 1 as rounded price is used`() {
        val buyable = TestBuyable(buyPrice = Amount(1_042))
        Model.gold = Amount(1_040)
        
        assertThat(buyable.effectiveBuyPossibleAmount.real).isEqualTo(1)
    }

}

private class TestBuyable(
    override val buyDescription: String = "testBuyDescription",
    override var buyPrice: Amount
) : Buyable
