package com.github.christophpickl.derbauer.logic


interface Screen {
    val message: String
    fun onCallback(callback: ScreenCallback)
}

interface Choice {
    val label: String
}

interface ScreenCallback {
    fun onMainScreen(screen: MainScreen)
    fun onLandBuy(screen: LandBuyScreen)
    fun onLandSell(screen: LandSellScreen)
    fun onEndTurn(screen: EndTurnScreen)
}

interface ChooseScreen<C : Choice> : Screen {
    val choices: List<C>
}

interface NumberInputScreen : Screen {

}

interface ScreenController<S : Screen> {

}

interface ChooseScreenController<C : Choice, S : ChooseScreen<C>> : ScreenController<S> {
    fun select(choice: C)
}

interface EnummedChoice<E : Enum<E>> : Choice {
    val enum: Enum<E>
}
