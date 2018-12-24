package com.github.christophpickl.derbauer2.military

import com.github.christophpickl.derbauer2.ScreenCallback
import com.github.christophpickl.derbauer2.home.HomeScreen
import com.github.christophpickl.derbauer2.ui.screen.CancelSupport
import com.github.christophpickl.derbauer2.ui.screen.ChooseScreen
import com.github.christophpickl.derbauer2.ui.screen.EnumChoice

class MilitaryScreen : ChooseScreen<MilitaryChoice>(
    messages = listOf(
        "Hey, don't look at me dude.",
        "Gonna kick some ass!",
        "Any problem can be solved with brute force and violence."
    ),
    choices = listOf(
        EnumChoice(MilitaryEnum.Attack, "Attack"),
        EnumChoice(MilitaryEnum.RecruiteSoldiers, "Recruite soldiers")
    )
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
}
