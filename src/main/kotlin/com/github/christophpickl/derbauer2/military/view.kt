package com.github.christophpickl.derbauer2.military

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.home.HomeView
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.PromptMode
import com.github.christophpickl.derbauer2.ui.view.CancelSupport
import com.github.christophpickl.derbauer2.ui.view.ChooseView
import com.github.christophpickl.derbauer2.ui.view.EnumChoice
import com.github.christophpickl.derbauer2.ui.view.InputView
import com.github.christophpickl.derbauer2.ui.view.View

class MilitaryView : ChooseView<MilitaryChoice>(
    messages = listOf(
        "Hey, don't look at me dude.",
        "Gonna kick some ass!",
        "Any problem can be solved with brute force and violence."
    ),
    choices = listOf(
        EnumChoice(MilitaryEnum.Attack, "Attack"),
        EnumChoice(MilitaryEnum.RecruiteSoldiers, "Recruite soldiers")
    ),
    additionalContent = "You've got:\n${Model.player.militaries.all.joinToString("\n") {
        "  ${it.labelPlural.capitalize()}: ${it.amount}"
    }}"
) {
    override val cancelSupport = CancelSupport.Enabled { HomeView() }
    override fun onCallback(callback: ViewCallback, choice: MilitaryChoice) {
        callback.onMilitary(choice)
    }
}

typealias MilitaryChoice = EnumChoice<MilitaryEnum>

enum class MilitaryEnum {
    Attack,
    RecruiteSoldiers
}

interface MilitaryCallback {
    fun onMilitary(choice: MilitaryChoice)
    fun doHire(militaryUnit: Military, amount: Int)
}

class AttackView(
    private val context: AttackContext
) : View {
    
    override val promptMode = PromptMode.Off
    override val cancelSupport = CancelSupport.Disabled
    override val renderContent get() = context.message

    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        // no-op
    }
}

class HireSoldiersView : InputView(
    "How many soldiers do you wanna hire?\n\n" +
        "1 soldier costs ${Model.player.militaries.soldiers.buyPrice} gold and ${Model.player.militaries.soldiers.costsPeople} people.\n\n" +
        "You can afford for ${Model.player.militaries.soldiers.effectiveBuyPossibleAmount} soldiers."
) {

    override fun onCallback(callback: ViewCallback, number: Int) {
        callback.doHire(Model.player.militaries.soldiers, number)
    }

    override val cancelSupport = CancelSupport.Enabled { MilitaryView() }
}
