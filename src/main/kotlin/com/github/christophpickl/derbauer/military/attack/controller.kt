package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.derbauer.military.Army
import com.github.christophpickl.derbauer.military.attack.target.AttackTarget
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.derbauer.ui.Renderer
import com.github.christophpickl.derbauer.ui.view.FeedbackView
import com.github.christophpickl.kpotpourri.common.random.RandomService
import com.github.christophpickl.kpotpourri.common.random.RealRandomService
import mu.KotlinLogging.logger
import kotlin.random.Random

class AttackController(
    private val renderer: Renderer,
    private val random: RandomService = RealRandomService
) : AttackCallback {

    private val log = logger {}
    private var threadId = 0
    private var currentTarget: AttackTarget? = null

    override fun onAttackTargetChosen(target: AttackTarget) {
        log.debug { "target choosen: $target" }
        currentTarget = target
        Model.currentView = ChooseAttackArmyView(PrepareAttackContext(
            armies = LinkedHashMap(Model.player.armies.all.filter { it.amount.isNotZero }.associate { it to null })
        ))
    }

    override fun onAttackArmyChosen(chosenArmy: Map<Army, Amount>) {
        val target = currentTarget ?: throw IllegalStateException("attack target not yet set")
        val context = AttackContext(
            target = target,
            armies = chosenArmy,
            enemies = target.enemies * Random.nextDouble(0.8, 1.2)
        )

        Model.currentView = AttackView(context)
        doBeginAttack(context)
    }

    private fun doBeginAttack(context: AttackContext) {
        log.debug { "begin attack: $context" }
        Thread(AttackThread(context, this, renderer), "Attack-${++threadId}").start()
    }

    override fun onBattleOver(context: AttackContext) {
        Model.history.attacked++

        val message = if (context.isBattleWon) {
            Model.history.attacksWon++
            var additionalMessage = ""
            context.target.enableNextTarget()?.let { target ->
                additionalMessage = "New attack target available: ${target.label}"
            }

            val loot = context.target.computeLoot(context)
            Model.gold += loot.goldEarned
            Model.food += loot.foodEarned
            Model.land += loot.landEarned
            "You won! This is your loot:\n\n" +
                "Gold stolen: ${loot.goldEarned.formatted}\n" +
                "Food stolen: ${loot.foodEarned.formatted}\n" +
                "Land captured: ${loot.landEarned.formatted}" +
                if (additionalMessage.isEmpty()) "" else "\n\n$additionalMessage"
        } else {
            "You lost! No loot for you, poor bastard."
        }

        context.message = FeedbackView.concatMessages(context.message, message)
    }
}

data class AttackLoot(
    val goldEarned: Amount,
    val landEarned: Amount,
    val foodEarned: Amount
    // spaeter fuer higher targets sogar: people, armies, upgrades (?)
)

interface AttackCallback {
    fun onAttackTargetChosen(target: AttackTarget)
    fun onAttackArmyChosen(chosenArmy: Map<Army, Amount>)
    fun onBattleOver(context: AttackContext)
}
