package com.github.christophpickl.derbauer2.military

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.home.HomeScreen
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.PromptMode
import com.github.christophpickl.derbauer2.ui.screen.CancelSupport
import com.github.christophpickl.derbauer2.ui.screen.ChooseScreen
import com.github.christophpickl.derbauer2.ui.screen.EnumChoice
import com.github.christophpickl.derbauer2.ui.screen.InputScreen
import com.github.christophpickl.derbauer2.ui.screen.Screen
import kotlin.random.Random

class MilitaryScreen : ChooseScreen<MilitaryChoice>(
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
    override val cancelSupport = CancelSupport.Enabled { HomeScreen() }
    override fun onCallback(callback: ScreenCallback, choice: MilitaryChoice) {
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
    fun doBeginAttack(context: AttackContext)
}

class AttackScreen : Screen {
    override val promptMode = PromptMode.Off
    override val cancelSupport = CancelSupport.Disabled

    private val context = AttackContext(
        enemies = (Random.nextDouble(0.4, 1.1) * Model.player.militaries.soldiers.amount).toInt()
    )
    override val renderContent get() = context.message

    override fun onCallback(callback: ScreenCallback, input: PromptInput) {
        if (!context.warStarted) {
            context.warStarted = true
            callback.doBeginAttack(context)
        }
    }
}

class HireSoldiersScreen : InputScreen(
    "How many soldiers do you wanna hire?\n\n" +
        "1 soldier costs ${Model.player.militaries.soldiers.buyPrice} gold and ${Model.player.militaries.soldiers.costsPeople} person\n\n" +
        "You can afford for ${Model.player.militaries.soldiers.effectiveBuyPossibleAmount} soldiers."
) {

    override fun onCallback(callback: ScreenCallback, number: Int) {
        callback.doHire(Model.player.militaries.soldiers, number)
    }

    override val cancelSupport = CancelSupport.Enabled { MilitaryScreen() }
}
