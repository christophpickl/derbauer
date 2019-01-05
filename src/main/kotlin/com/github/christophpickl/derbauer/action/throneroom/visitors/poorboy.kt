package com.github.christophpickl.derbauer.action.throneroom.visitors

import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomChoice
import com.github.christophpickl.derbauer.action.throneroom.ThroneRoomVisitor
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.Alert
import com.github.christophpickl.derbauer.ui.AlertType
import com.github.christophpickl.kpotpourri.common.math.KMath
import kotlin.random.Random

class PoorBoyVisitor : ThroneRoomVisitor<PoorBoyChoice> {

    companion object {
        private fun buildGoldAmounts() = Values.actions.throneRoom.boyDemandsOfRelativeWealth.map {
            Model.player.relativeWealthBy(it)
        }
    }

    private val goldAmounts = buildGoldAmounts()
    override fun condition() = true
    override val message = "A poor boy enters carefully the room and begs for ${goldAmounts[0].formatted} gold."
    override val choosePrompt = "How do you feel about that boy, my lord?"

    override val choices = listOf(
        PoorBoyChoice(PoorBoyDecision.GiveMoneyDecision(goldAmounts[0], PoorBoyMoneySize.Little),
            karmaEffect = Values.karma.throneRoom.boyMoneyLittle),
        PoorBoyChoice(PoorBoyDecision.GiveMoneyDecision(goldAmounts[1], PoorBoyMoneySize.Medium),
            karmaEffect = Values.karma.throneRoom.boyMoneyMedium),
        PoorBoyChoice(PoorBoyDecision.GiveMoneyDecision(goldAmounts[2], PoorBoyMoneySize.Much),
            karmaEffect = Values.karma.throneRoom.boyMoneyMuch),
        PoorBoyChoice(PoorBoyDecision.SendAwayDecision, karmaEffect = Values.karma.throneRoom.boySendAway),
        PoorBoyChoice(PoorBoyDecision.ThrowDungeonDecision, karmaEffect = Values.karma.throneRoom.boyThrowDungeon)
    )

    override fun choose(choice: PoorBoyChoice) =
        when (choice.decision) {
            is PoorBoyDecision.GiveMoneyDecision -> {
                giveMoney(choice.decision)
            }
            PoorBoyDecision.SendAwayDecision -> {
                "You greedy bastard."
            }
            PoorBoyDecision.ThrowDungeonDecision -> {
                Model.people -= 1
                "Your people gonna hate you for this."
            }
        }

    private fun giveMoney(decision: PoorBoyDecision.GiveMoneyDecision): String? {
        if (decision.amount > Model.gold) {
            Alert.show(AlertType.NotEnoughGold)
            return null
        }
        
        Model.gold -= decision.amount

        val probabilityReward = when (decision.size) {
            PoorBoyMoneySize.Little -> 0.05
            PoorBoyMoneySize.Medium -> 0.15
            PoorBoyMoneySize.Much -> 0.4
        } + KMath.max(Model.global.karma / 10, 0.2)

        return if (Random.nextDouble() < probabilityReward) {
            val reward = Amount(KMath.min(Values.actions.throneRoom.boyMinReward,
                Model.player.relativeWealthBy(Values.actions.throneRoom.boyRewardRelativeWealth).real))
            Model.land += reward
            "Because of your generosity you get ${reward.formatted} land for free."
        } else {
            "The kid thankfully takes the ${decision.amount.formatted} gold and quickly leaves the room."
        }
    }
}

class PoorBoyChoice(
    val decision: PoorBoyDecision,
    override val karmaEffect: Double
) : ThroneRoomChoice {
    override val label: String = decision.label
}

sealed class PoorBoyDecision(
    val label: String
) {
    data class GiveMoneyDecision(
        val amount: Amount,
        val size: PoorBoyMoneySize
    ) : PoorBoyDecision("Give him ${amount.formatted} gold.")

    object SendAwayDecision : PoorBoyDecision("Send him away.")
    object ThrowDungeonDecision : PoorBoyDecision("Throw this scum into the dungeon!")
}

enum class PoorBoyMoneySize {
    Little,
    Medium,
    Much
}
