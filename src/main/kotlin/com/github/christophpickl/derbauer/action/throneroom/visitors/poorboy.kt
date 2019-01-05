package com.github.christophpickl.derbauer.action.throneroom.visitors

import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomChoice
import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomVisitor
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model
import kotlin.random.Random

class PoorBoyVisitor : ThroneRoomVisitor<PoorBoyChoice> {

    companion object {
        private fun buildGoldAmounts() = listOf(Amount(1), Amount(10), Amount(100))
    }

    private val goldAmounts = buildGoldAmounts()
    override fun condition() = true
    override val message = "A poor boy enters carefully the room and begs for ${goldAmounts[0].formatted} gold."
    override val choosePrompt = "How do you feel about that boy, my lord?"

    override val choices = listOf(
        PoorBoyChoice(PoorBoyDecision.GiveMoneyDecision(goldAmounts[0], PoorBoyMoneySize.Little)),
        PoorBoyChoice(PoorBoyDecision.GiveMoneyDecision(goldAmounts[1], PoorBoyMoneySize.Medium)),
        PoorBoyChoice(PoorBoyDecision.GiveMoneyDecision(goldAmounts[2], PoorBoyMoneySize.Much)),
        PoorBoyChoice(PoorBoyDecision.SendAwayDecision),
        PoorBoyChoice(PoorBoyDecision.ThrowDungeonDecision)
    )

    override fun choose(choice: PoorBoyChoice) =
        when (choice.decision) {
            is PoorBoyDecision.GiveMoneyDecision -> {
                giveMoney(choice.decision)
            }
            PoorBoyDecision.SendAwayDecision -> "You greedy bastard."
            PoorBoyDecision.ThrowDungeonDecision -> {
                Model.people -= 1
                "Your people gonna hate you for this."
            }
        }

    private fun giveMoney(decision: PoorBoyDecision.GiveMoneyDecision): String {
        Model.gold -= decision.amount

        val probReward = when (decision.size) {
            PoorBoyMoneySize.Little -> 0.05
            PoorBoyMoneySize.Medium -> 0.15
            PoorBoyMoneySize.Much -> 0.4
        }
        val isRewardGranted = Random.nextDouble(0.0, 1.0) < probReward
        return if (isRewardGranted) {
            Model.land += 5
            "Because of your generosity you get 5 land :)"
        } else {
            "The kid thankfully takes the ${decision.amount.formatted} gold and quickly leaves the room."
        }
    }
}

class PoorBoyChoice(
    val decision: PoorBoyDecision
) : ThroneRoomChoice {
    override val label: String = decision.label
}

sealed class PoorBoyDecision(
    val label: String
) {
    data class GiveMoneyDecision(
        val amount: Amount,
        val size: PoorBoyMoneySize
    ) : PoorBoyDecision("Givem him ${amount.formatted} gold.")

    object SendAwayDecision : PoorBoyDecision("Send him away.")
    object ThrowDungeonDecision : PoorBoyDecision("Throw this scum into the dungeon!")
}

enum class PoorBoyMoneySize {
    Little,
    Medium,
    Much
}
