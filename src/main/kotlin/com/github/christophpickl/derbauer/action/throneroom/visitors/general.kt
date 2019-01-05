package com.github.christophpickl.derbauer.action.throneroom.visitors

import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomChoice
import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomVisitor
import com.github.christophpickl.derbauer.model.Model
import kotlin.random.Random

class GeneralVisitor : ThroneRoomVisitor<GeneralChoice> {
    override fun condition() = Model.player.militaries.soldiers.amount > 0
    override val message = "A general enters, demanding a soldier to join his personal guard."
    override val choosePrompt = "Do you agree with his wish?"
    override val choices = listOf(
        GeneralChoice(GeneralDecision.Agree),
        GeneralChoice(GeneralDecision.Decline)
    )

    override fun choose(choice: GeneralChoice) =
        when (choice.decision) {
            GeneralDecision.Agree -> {
                Model.player.militaries.soldiers.amount--
                if (Random.nextDouble(0.0, 1.0) < 0.3) {
                    Model.gold += 200
                    "The general approaches you, shakes your hand and gives you 200 gold."
                } else {
                    "One of your best man joins him and both leave the room."
                }
            }
            GeneralDecision.Decline -> {
                "Pah! Just wait..."
            }
        }
}

class GeneralChoice(
    val decision: GeneralDecision
) : ThroneRoomChoice {
    override val label: String = decision.label
}

enum class GeneralDecision(
    val label: String
) {
    Agree("Agree."),
    Decline("Decline!")
}
