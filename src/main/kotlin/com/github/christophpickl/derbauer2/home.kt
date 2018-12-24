package com.github.christophpickl.derbauer2

import com.github.christophpickl.derbauer2.build.BuildScreen
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.trade.TradeScreen
import com.github.christophpickl.derbauer2.ui.screen.CancelSupport
import com.github.christophpickl.derbauer2.ui.screen.ChooseScreen
import com.github.christophpickl.derbauer2.ui.screen.EnumChoice

class HomeScreen : ChooseScreen<HomeChoice>(
    messages = listOf(
        "What are we up to today?",
        "What can I do for you, master?",
        "Your wish is my command."
    ),
    choices = listOf(
        EnumChoice(HomeEnum.Trade, "Trade"),
        EnumChoice(HomeEnum.Build, "Build"),
        EnumChoice(HomeEnum.EndTurn, "End Turn")
    )
) {
    override val cancelSupport = CancelSupport.Disabled

    override fun onCallback(callback: ScreenCallback, choice: HomeChoice) {
        callback.onHomeEnum(choice)
    }
}

typealias HomeChoice = EnumChoice<HomeEnum>

enum class HomeEnum {
    Trade,
    Build,
    EndTurn
}

interface HomeScreenCallback {
    fun onHomeEnum(choice: HomeChoice)
}

class HomeController : HomeScreenCallback {
    override fun onHomeEnum(choice: HomeChoice) {
        when (choice.enum) {
            HomeEnum.Trade -> {
                Model.screen = TradeScreen()
            }
            HomeEnum.Build -> {
                Model.screen = BuildScreen()
            }
            HomeEnum.EndTurn -> {
                Model.global.day++
                Model.screen = EndTurnScreen()
            }
        }.enforceWhenBranches()
    }

}
