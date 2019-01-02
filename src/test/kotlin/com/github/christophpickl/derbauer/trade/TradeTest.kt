package com.github.christophpickl.derbauer.trade

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.isAmountEqualTo
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.TradeableResource
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class TradeTest {

    fun `Given enough food When sell Then sold and gold income`() {
        Model.food = Amount.one
        Model.gold = Amount.zero

        tradeFood(BuySell.Sell, 1)

        assertThat(Model.food).isAmountEqualTo(0)
        assertThat(Model.gold).isEqualTo(Model.player.resources.food.sellPrice)
    }

    fun `Given not enough food When sell Then nothing changes`() {
        Model.food = Amount.zero
        Model.gold = Amount.zero

        tradeFood(BuySell.Sell, 1)

        assertThat(Model.food).isAmountEqualTo(0)
        assertThat(Model.gold).isAmountEqualTo(0)
    }

    fun `Given enough gold and capacity When buy food Then food added and gold subtracted`() {
        Model.gold = Model.player.resources.food.buyPrice
        Model.player.buildings.granaries.amount = Amount.one
        Model.food = Amount.zero

        tradeFood(BuySell.Buy, 1)

        assertThat(Model.food).isAmountEqualTo(1)
        assertThat(Model.gold).isAmountEqualTo(0)
    }

    private fun tradeFood(buySell: BuySell, amount: Long) {
        trade(Model.player.resources.food, buySell, amount)
    }

    private fun trade(resource: TradeableResource, buySell: BuySell, amount: Long) {
        TradeController().doTrade(TradeableChoice(resource, buySell), amount)
    }

}
