package com.github.christophpickl.derbauer.logic

class LandBuyScreen(
    state: GameState
) : NumberInputScreen {
    override val message = "How much land do you wanna buy?\n" +
        "1 costs ${state.prizes.landBuy} gold, you can afford ${state.affordableLand} land."

    override fun onCallback(callback: ScreenCallback) {
        callback.onLandBuy(this)
    }
}

class LandSellScreen(
    state: GameState
) : NumberInputScreen {
    override val message = "How much land do you wanna sell?\n1 for ${state.prizes.landSell} gold, you've got ${state.player.land} land."
    override fun onCallback(callback: ScreenCallback) {
        callback.onLandSell(this)
    }
}

class FoodBuyScreen(
    state: GameState
) : NumberInputScreen {
    override val message = "How much food do you wanna buy?\n" +
        "1 costs ${state.prizes.foodBuy} gold, you can afford ${state.affordableFood} food."

    override fun onCallback(callback: ScreenCallback) {
        callback.onFoodBuy(this)
    }
}

class FoodSellScreen(
    state: GameState
) : NumberInputScreen {
    override val message = "How much food do you wanna sell?\n1 for ${state.prizes.foodSell} gold, you've got ${state.player.food} food."
    override fun onCallback(callback: ScreenCallback) {
        callback.onFoodSell(this)
    }
}
