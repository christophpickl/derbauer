package com.github.christophpickl.derbauer.logic.screens


interface Screen {
    val message: String
    val enableCancelOnEnter: Boolean

    fun onCallback(callback: ScreenCallback)
}

interface Choice {
    val label: String
}

interface ScreenCallback {
    fun onHomeScreen(screen: HomeScreen)

    fun onTrade(screen: TradeScreen)
    fun onLandBuy(screen: LandBuyScreen)
    fun onLandSell(screen: LandSellScreen)
    fun onFoodBuy(screen: FoodBuyScreen)
    fun onFoodSell(screen: FoodSellScreen)

    fun onBuild(screen: BuildScreen)
    fun onUpgrade(screen: UpgradeScreen)
    
    fun onEndTurn(screen: EndTurnScreen)
    fun onGameOver(screen: GameOverScreen)
    
}

interface ChooseScreen<C : Choice> : Screen {
    override val enableCancelOnEnter get() = true
    val choices: List<C>
}

interface NumberInputScreen : Screen {
    override val enableCancelOnEnter get() = true
}

interface ScreenController<S : Screen> {

}

interface ChooseScreenController<C : Choice, S : ChooseScreen<C>> : ScreenController<S> {
    fun select(choice: C)
}

interface EnummedChoice<E : Enum<E>> : Choice {
    val enum: Enum<E>
}
