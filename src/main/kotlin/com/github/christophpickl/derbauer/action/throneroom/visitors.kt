package com.github.christophpickl.derbauer.action.throneroom

import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model
import kotlin.random.Random

interface ThroneRoomVisitor {
    val message: String
    val choices: List<ThroneRoomChoice>
    fun condition(): Boolean
    fun choose(choice: ThroneRoomChoice): String
}

class PoorBoyVisitor : ThroneRoomVisitor {

    override fun condition() = true
    override val message = "A poor boy enters carefully the room and begs for 1 gold coin.\n" +
        "How do you wish to proceed, my lord?"
    override val choices = listOf(
        ThroneRoomChoice(1, "Give him 1 gold."),
        ThroneRoomChoice(2, "Give him 10 gold."),
        ThroneRoomChoice(3, "Give him 100 gold."),
        ThroneRoomChoice(4, "Send him away."),
        ThroneRoomChoice(5, "Throw this scum into the dungeon!")
    )

    override fun choose(choice: ThroneRoomChoice) =
        when (choice.id) {
            1, 2, 3 -> {
                giveMoney(choice.id)
            }
            4 -> {
                "You greedy bastard."
            }
            5 -> {
                Model.people -= 1
                "Your people gonna hate you for this."
            }
            else -> throw IllegalArgumentException("Unhandled choice ID: ${choice.id}")
        }

    private fun giveMoney(choiceId: Int): String {
        val goldAmountToGive = Amount(if (choiceId == 1) 1L else if (choiceId == 2) 10L else 100L)
        Model.gold -= goldAmountToGive

        val probReward = when (choiceId) {
            1 -> 0.05; 2 -> 0.15; else -> 0.4
        }
        val isRewardGranted = Random.nextDouble(0.0, 1.0) < probReward
        return if (isRewardGranted) {
            Model.land += 5
            "Because of your generosity you get 5 land :)"
        } else {
            "The kid thankfully takes the ${goldAmountToGive.formatted} gold and quickly leaves the room."
        }
    }
}

// FIXME write test first; then change ThroneRoomChoice to carry some generic data with it.
//sealed class ThroneRomeAction {
//    class GiveGold(amount: Int) : ThroneRomeAction()
//    object SendAway : ThroneRomeAction()
//    object ThrowToDungeon : ThroneRomeAction()
//}

class GeneralDemandVisitor : ThroneRoomVisitor {
    override fun condition() = Model.player.militaries.soldiers.amount > 0
    override val message = "A general enters, demanding a soldier to join his personal guard."
    override val choices = listOf(
        ThroneRoomChoice(1, "Agree."),
        ThroneRoomChoice(2, "Decline.")
    )

    override fun choose(choice: ThroneRoomChoice) =
        when (choice.id) {
            1 -> {
                Model.player.militaries.soldiers.amount--
                if (Random.nextDouble(0.0, 1.0) < 0.3) {
                    Model.gold += 200
                    "The general approaches you, shakes your hand and gives you 200 gold."
                } else {
                    "One of your best man joins him and both leave the room."
                }
            }
            2 -> {
                "Pah! Just wait..."
            }
            else -> throw IllegalArgumentException("Unhandled choice ID: ${choice.id}")
        }
}
