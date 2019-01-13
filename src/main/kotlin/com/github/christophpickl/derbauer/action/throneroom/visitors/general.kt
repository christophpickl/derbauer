package com.github.christophpickl.derbauer.action.throneroom.visitors

import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomChoice
import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomVisitor
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.kpotpourri.common.math.KMath
import kotlin.random.Random

class GeneralVisitor : ThroneRoomVisitor<GeneralChoice> {


    override val message = "A general enters, demanding a soldier to join his personal guard.\n\n" +
        "Do you agree with his wish?"

    override val choices = listOf(
        GeneralChoice(GeneralDecision.Agree, karmaEffect = Values.karma.throneRoom.generalAgree),
        GeneralChoice(GeneralDecision.Decline, karmaEffect = Values.karma.throneRoom.generalDecline)
    )
    
    override fun condition() = Model.player.armies.soldiers.amount > 0

    override fun choose(choice: GeneralChoice) =
        when (choice.decision) {
            GeneralDecision.Agree -> {
                executeAgreeToGeneral()
            }
            GeneralDecision.Decline -> {
                "Pah! Just wait..."
            }
        }

    private fun executeAgreeToGeneral(): String {
        Model.player.armies.soldiers.amount--

        val probabilityReward = 0.3 + KMath.max(Model.global.karma / 10, 0.2)

        return if (Random.nextDouble() <= probabilityReward) {
            val reward = Amount(KMath.max(Values.actions.throneRoom.generalMinReward,
                Model.player.relativeWealthBy(Values.actions.throneRoom.generalRewardInGoldRelativeToWealth).real))
            Model.gold += reward
            "The general approaches you, shakes your hand and gives you ${reward.formatted} gold."
        } else {
            "One of your best man joins him and both leave the room."
        }
    }
}

class GeneralChoice(
    val decision: GeneralDecision,
    override val karmaEffect: Double
) : ThroneRoomChoice {
    override val label: String = decision.label
}

enum class GeneralDecision(
    val label: String
) {
    Agree("Agree."),
    Decline("Decline!")
}
