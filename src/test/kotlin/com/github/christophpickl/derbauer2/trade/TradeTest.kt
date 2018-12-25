package com.github.christophpickl.derbauer2.trade

import com.github.christophpickl.derbauer2.TestModelListener
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.TradeableResource
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class TradeTest {

    fun `Given enough food When sell Then sold and gold income`() {
        Model.food = 1
        Model.gold = 0

        tradeFood(BuySell.Sell, 1)

        assertThat(Model.food).isEqualTo(0)
        assertThat(Model.gold).isEqualTo(Model.player.resources.food.sellPrice)
    }

    fun `Given not enough food When sell Then nothing changes`() {
        Model.food = 0
        Model.gold = 0

        tradeFood(BuySell.Sell, 1)

        assertThat(Model.food).isEqualTo(0)
        assertThat(Model.gold).isEqualTo(0)
    }

    fun `Given enough gold and capacity When buy food Then food added and gold subtracted`() {
        Model.gold = Model.player.resources.food.buyPrice
        Model.player.buildings.granaries.amount = 1
        Model.food = 0

        tradeFood(BuySell.Buy, 1)

        assertThat(Model.food).isEqualTo(1)
        assertThat(Model.gold).isEqualTo(0)
    }

    private fun tradeFood(buySell: BuySell, amount: Int) {
        trade(Model.player.resources.food, buySell, amount)
    }

    private fun trade(resource: TradeableResource, buySell: BuySell, amount: Int) {
        TradeController().doTrade(TradableChoice(resource, buySell), amount)
    }

}
