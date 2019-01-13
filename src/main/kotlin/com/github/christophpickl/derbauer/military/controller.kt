package com.github.christophpickl.derbauer.military

import com.github.christophpickl.derbauer.military.attack.AttackContext
import com.github.christophpickl.derbauer.military.attack.AttackThread
import com.github.christophpickl.derbauer.military.attack.AttackView
import com.github.christophpickl.derbauer.military.attack.PrepareAttackContext
import com.github.christophpickl.derbauer.military.attack.PrepareAttackView
import com.github.christophpickl.derbauer.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer.misc.enforceWhenBranches
import com.github.christophpickl.derbauer.misc.validateChoice
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.derbauer.ui.Alert
import com.github.christophpickl.derbauer.ui.AlertType
import com.github.christophpickl.derbauer.ui.Renderer
import mu.KotlinLogging.logger
import kotlin.random.Random

class MilitaryController(
    private val renderer: Renderer
) : MilitaryCallback {

    private val log = logger {}
    private var threadId = 0

    override fun onMilitary(choice: MilitaryChoice) {
        log.debug { "military action: $choice" }
        when (choice) {
            MilitaryChoice.Attack -> {
                prepareAttack()
            }
            is MilitaryChoice.Hire -> {
                Model.currentView = HireView(choice.army)

            }
        }.enforceWhenBranches()
    }

    private fun prepareAttack() {
        if (Model.player.armies.totalAmount.isZero) {
            Alert.show(AlertType.NoArmy)
            return
        }
        Model.currentView = PrepareAttackView(PrepareAttackContext(
            armies = LinkedHashMap(Model.player.armies.all.filter { it.amount.isNotZero }.associate { it to null })
        ))
    }

    override fun executeAttack(chosenArmy: Map<Army, Amount>) {
        val context = AttackContext(
            armies = chosenArmy,
            enemies = Model.player.armies.totalAmount * Random.nextDouble(0.4, 1.1)
        )
        Model.currentView = AttackView(context)
        doBeginAttack(context)
    }

    private fun doBeginAttack(context: AttackContext) {
        log.debug { "begin attack: $context" }
        Thread(AttackThread(context, renderer), "Attack-${++threadId}").start()
    }

    override fun doHire(army: Army, amount: Long) {
        log.debug { "want to hire: $amount $army" }
        val totalPrice = army.buyPrice.rounded * amount
        val totalPeople = army.costsPeople.rounded * amount
        if (isValid(totalPrice, totalPeople, amount)) {
            army.amount += amount
            Model.gold -= totalPrice
            Model.people -= totalPeople
            Model.currentView = MilitaryView()
        }
    }

    private fun isValid(totalPrice: Long, totalPeople: Long, amountToHire: Long) = validateChoice(listOf(
        SimpleChoiceValidation(
            condition = { Model.gold >= totalPrice },
            alertType = AlertType.NotEnoughGold
        ),
        SimpleChoiceValidation(
            condition = { Model.people >= totalPeople },
            alertType = AlertType.NotEnoughPeople
        ),
        SimpleChoiceValidation(
            condition = { Model.totalArmyCapacity >= amountToHire },
            alertType = AlertType.NotEnoughCapacity
        )
    ))

}
