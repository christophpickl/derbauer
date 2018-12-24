package com.github.christophpickl.derbauer2.home

import com.github.christophpickl.derbauer2.ScreenCallback
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
        EnumChoice(HomeEnum.Upgrade, "Upgrade"),
        EnumChoice(HomeEnum.Military, "Military"),
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
    Upgrade,
    Military,
    EndTurn
}

interface HomeScreenCallback {
    fun onHomeEnum(choice: HomeChoice)
    fun goEndTurnReport()
}
