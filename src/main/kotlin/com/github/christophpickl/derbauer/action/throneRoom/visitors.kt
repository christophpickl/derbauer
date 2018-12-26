package com.github.christophpickl.derbauer.action.throneRoom

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
                val goldAmountToGive = if (choice.id == 1) 1 else if (choice.id == 2) 10 else 100
                Model.gold -= goldAmountToGive

                val probReward = when (choice.id) {
                    1 -> 0.05; 2 -> 0.15; else -> 0.4
                }
                val isRewardGranted = Random.nextDouble(0.0, 1.0) < probReward
                if (isRewardGranted) {
                    Model.land += 5
                    "Because of your generosity you get 5 land :)"
                } else {
                    "The kid thankfully takes the $goldAmountToGive gold and quickly leaves the room."
                }
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
}

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
