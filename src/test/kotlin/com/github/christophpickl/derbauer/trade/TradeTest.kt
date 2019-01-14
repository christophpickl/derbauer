package com.github.christophpickl.derbauer.trade

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.buysell.BuySell
import com.github.christophpickl.derbauer.isAmountEqualTo
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.derbauer.resource.BuyAndSellableResource
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

    fun `Given not really enough gold When buy food Then succeed because rounded price used`() {
        Model.player.resources.food.buyPrice = Amount(1_999)
        Model.gold = Amount(1_990)
        Model.player.buildings.granaries.amount = Amount.one

        tradeFood(BuySell.Buy, 1)

        assertThat(Model.food).isAmountEqualTo(1)
        assertThat(Model.gold).isAmountEqualTo(0)
    }

    fun `Given a bit more gold than needed When buy food Then some gold left because rounded price is used`() {
        Model.player.resources.food.buyPrice = Amount(1_010)
        Model.gold = Amount(1_017)
        Model.player.buildings.granaries.amount = Amount.one

        tradeFood(BuySell.Buy, 1)

        assertThat(Model.food).isAmountEqualTo(1)
        assertThat(Model.gold).isAmountEqualTo(7)
    }

    fun `Given enough food When sell Then sold and gold income with rounded price`() {
        Model.player.resources.food.sellPrice = Amount(1_042)
        Model.food = Amount.one
        Model.gold = Amount.zero

        tradeFood(BuySell.Sell, 1)

        assertThat(Model.food).isAmountEqualTo(0)
        assertThat(Model.gold).isAmountEqualTo(1_040)
    }
    
    private fun tradeFood(buySell: BuySell, amount: Long) {
        trade(Model.player.resources.food, buySell, amount)
    }

    private fun trade(resource: BuyAndSellableResource, buySell: BuySell, amount: Long) {
        TradeController().doTrade(TradeableChoice(resource, buySell), amount)
    }

}
