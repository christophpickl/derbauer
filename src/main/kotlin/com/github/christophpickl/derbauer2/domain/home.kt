package com.github.christophpickl.derbauer2.domain

import com.github.christophpickl.derbauer2.misc.CancelSupport
import com.github.christophpickl.derbauer2.misc.ChooseScreen
import com.github.christophpickl.derbauer2.misc.EnumChoice
import com.github.christophpickl.derbauer2.misc.ScreenCallback
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.state.State

class HomeScreen : ChooseScreen<HomeChoice>(
    message = "This is home, waiting for input",
    choices = listOf(
        EnumChoice(HomeEnum.Trade, "Trade"),
        EnumChoice(HomeEnum.EndTurn, "End Turn")
    )
) {
    override val cancelSupport = CancelSupport.Disabled

    override fun onCallback(callback: ScreenCallback, choice: HomeChoice) {
        when (choice.enum) {
            HomeEnum.Trade -> callback.onTrade()
            HomeEnum.EndTurn -> callback.onEndTurn()
        }.enforceWhenBranches()
    }
}

typealias HomeChoice = EnumChoice<HomeEnum>

enum class HomeEnum {
    Trade,
    EndTurn
}

interface HomeScreenCallback {
    fun onEndTurn()
    fun onTrade()
}

class HomeController : HomeScreenCallback {
    override fun onTrade() {
        State.screen = TradeScreen()
    }

    override fun onEndTurn() {
        State.global.day++
        State.screen = EndTurnScreen()
    }
}
