package com.github.christophpickl.derbauer.logic

import javax.inject.Inject

class MainScreen : ChooseScreen<MainScreen.MainScreenChoice> {
    override val message = "What do you wanna do now?"
    override val choices = MainScreenChoice.values().toList()
    override fun onCallback(callback: ScreenCallback) {
        callback.onMainScreen(this)
    }

    enum class MainScreenChoice(
        override val label: String
    ) : Choice {
        BuyLand("Buy Land"),
        SellLand("Sell Land")
    }
}

class MainScreenController @Inject constructor(
    private val state: GameState
) : ChooseScreenController<MainScreen.MainScreenChoice, MainScreen> {

    override fun select(choice: MainScreen.MainScreenChoice) {
        val nextScreen = when (choice) {
            MainScreen.MainScreenChoice.BuyLand -> LandBuyScreen()
            MainScreen.MainScreenChoice.SellLand -> LandSellScreen()
        }
        state.screen = nextScreen
    }

    fun buyLand(amount: Int) {
        val costs = state.prizes.landBuy * amount
        if (costs > state.player.gold) {
            return beep()
        }
        state.player.land += amount
        state.player.gold -= costs
        state.screen = MainScreen()
    }

    fun sellLand(amount: Int) {
        if (state.player.land < amount) {
            return beep()
        }
        state.player.land -= amount
        state.player.gold += state.prizes.landSell * amount
        state.screen = MainScreen()
    }

}

class LandBuyScreen : NumberInputScreen {
    override val message = "How much land do you wanna buy?"
    override fun onCallback(callback: ScreenCallback) {
        callback.onLandBuy(this)
    }
}

class LandSellScreen : NumberInputScreen {
    override val message = "How much land do you wanna buy?"
    override fun onCallback(callback: ScreenCallback) {
        callback.onLandSell(this)
    }
}
