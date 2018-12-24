package com.github.christophpickl.derbauer.trade

import com.github.christophpickl.derbauer.logic.ChooseScreen
import com.github.christophpickl.derbauer.logic.EnummedChoice
import com.github.christophpickl.derbauer.logic.NumberInputScreen
import com.github.christophpickl.derbauer.logic.ScreenCallback
import com.github.christophpickl.derbauer.logic.formatNumber
import com.github.christophpickl.derbauer.model.State

@Deprecated(message = "v2")
enum class TradeEnum {
    BuyLand,
    SellLand,
    BuyFood,
    SellFood
}

@Deprecated(message = "v2")
class TradeChoice(
    override val enum: Enum<TradeEnum>,
    override val label: String
) : EnummedChoice<TradeEnum>

@Deprecated(message = "v2")
class TradeScreen : ChooseScreen<TradeChoice> {

    private val messages = listOf(
        "Try not to get broke, huh?!",
        "Got anything useful?",
        "Psssst, over here! Looking for something?"
    )
    override val message = messages.random()

    //@formatter:off
    override val choices
        get() = listOf(
            TradeChoice(TradeEnum.BuyLand,  "Buy land  ... ${formatNumber(State.prices.trade.landBuy, 2)} $"),
            TradeChoice(TradeEnum.SellLand, "Sell land ... ${formatNumber(State.prices.trade.landSell, 2)} $"),
            TradeChoice(TradeEnum.BuyFood,  "Buy food  ... ${formatNumber(State.prices.trade.foodBuy, 2)} $"),
            TradeChoice(TradeEnum.SellFood, "Sell food ... ${formatNumber(State.prices.trade.foodSell, 2)} $")
        )
    //@formatter:on

    override fun onCallback(callback: ScreenCallback) {
        callback.onTrade(this)
    }

}

@Deprecated(message = "v2")
class LandBuyScreen : NumberInputScreen {

    override val message = "How much land do you wanna buy?\n\n" +
        "1 costs ${State.prices.trade.landBuy} gold, you can afford ${State.affordableLand} land."

    override fun onCallback(callback: ScreenCallback) {
        callback.onLandBuy(this)
    }
}

@Deprecated(message = "v2")
class LandSellScreen : NumberInputScreen {
    override val message = "How much land do you wanna sell?\n\n" +
        "1 for ${State.prices.trade.landSell} gold, you've got ${State.player.landAvailable} land available."
    override fun onCallback(callback: ScreenCallback) {
        callback.onLandSell(this)
    }
}

@Deprecated(message = "v2")
class FoodBuyScreen : NumberInputScreen {
    override val message = "How much food do you wanna buy?\n\n" +
        "1 costs ${State.prices.trade.foodBuy} gold, you can afford ${State.affordableFood} food."

    override fun onCallback(callback: ScreenCallback) {
        callback.onFoodBuy(this)
    }
}

@Deprecated(message = "v2")
class FoodSellScreen : NumberInputScreen {
    override val message = "How much food do you wanna sell?\n\n" +
        "1 for ${State.prices.trade.foodSell} gold, you've got ${State.player.food} food."
    override fun onCallback(callback: ScreenCallback) {
        callback.onFoodSell(this)
    }
}
