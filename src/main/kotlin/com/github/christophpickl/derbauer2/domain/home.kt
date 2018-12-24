package com.github.christophpickl.derbauer2.domain

import com.github.christophpickl.derbauer2.CancelSupport
import com.github.christophpickl.derbauer2.ChooseScreen
import com.github.christophpickl.derbauer2.EnumChoice
import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.state.Model
import com.github.christophpickl.derbauer2.trade.TradeScreen

class HomeScreen : ChooseScreen<HomeChoice>(
    messages = listOf(
        "What are we up to today?",
        "What can I do for you, master?",
        "Your wish is my command."
    ),
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
        Model.screen = TradeScreen()
    }

    override fun onEndTurn() {
        Model.global.day++
        Model.screen = EndTurnScreen()
    }
}
