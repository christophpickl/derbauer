package com.github.christophpickl.derbauer2.home

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.ui.view.CancelSupport
import com.github.christophpickl.derbauer2.ui.view.ChooseView
import com.github.christophpickl.derbauer2.ui.view.EnumChoice

class HomeView : ChooseView<HomeChoice>(
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

    override fun onCallback(callback: ViewCallback, choice: HomeChoice) {
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

interface HomeCallback {
    fun onHomeEnum(choice: HomeChoice)
    fun goEndTurnReport()
}
