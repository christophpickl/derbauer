package com.github.christophpickl.derbauer2.home

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.view.CancelSupport
import com.github.christophpickl.derbauer2.ui.view.ChooseView
import com.github.christophpickl.derbauer2.ui.view.EnumChoice
import java.util.*

class HomeView : ChooseView<HomeChoice>(
    messages = listOf(
        "What are we up to today?",
        "What can I do for you, master?",
        "Your wish is my command."
    ),
    choices = LinkedList<HomeChoice>().apply {
        this += EnumChoice(HomeEnum.Trade, "Trade")
        this += EnumChoice(HomeEnum.Build, "Build")
        this += EnumChoice(HomeEnum.Upgrade, "Upgrade")
        if (Model.feature.military.isMilitaryEnabled) {
            this += EnumChoice(HomeEnum.Military, "Military")
        }
        this += EnumChoice(HomeEnum.Action, "Action")
        this += EnumChoice(HomeEnum.EndTurn, "End Turn", zeroChoice = true)
    }
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
    Action,
    EndTurn
}

interface HomeCallback {
    fun onHomeEnum(choice: HomeChoice)
    fun goEndTurnReport()
}
