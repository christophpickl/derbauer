package com.github.christophpickl.derbauer.home

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.data.Texts
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.ChooseView
import com.github.christophpickl.derbauer.ui.view.EnumChoice
import java.util.*

class HomeView : ChooseView<HomeChoice>(
    choosePrompt = "What is your concern?",
    messages = Texts.homeMessages,
    choices = LinkedList<HomeChoice>().apply {
        this += EnumChoice(HomeEnum.Trade, "Trade")
        this += EnumChoice(HomeEnum.Build, "Build")
        if (Model.features.upgrade.menu.isEnabled()) {
            this += EnumChoice(HomeEnum.Upgrade, "Upgrade")
        }
        if (Model.features.military.menu.isEnabled()) {
            this += EnumChoice(HomeEnum.Military, "Military")
        }
        if (Model.actions.all.isNotEmpty()) {
            this += EnumChoice(HomeEnum.Action, "Action")
        }
        this += EnumChoice(HomeEnum.EndTurn, "End Turn", zeroChoice = true)
    },
    cancelSupport = CancelSupport.Disabled
) {

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
