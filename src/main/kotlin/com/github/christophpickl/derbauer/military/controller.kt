package com.github.christophpickl.derbauer.military

import com.github.christophpickl.derbauer.military.attack.AttackContext
import com.github.christophpickl.derbauer.military.attack.AttackThread
import com.github.christophpickl.derbauer.military.attack.AttackView
import com.github.christophpickl.derbauer.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer.misc.enforceWhenBranches
import com.github.christophpickl.derbauer.misc.validateChoice
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model
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
                Model.currentView = HireView(choice.military)

            }
        }.enforceWhenBranches()
    }

    private fun prepareAttack() {
        if (Model.player.militaries.totalAmount.isZero) {
            Alert.show(AlertType.NoMilitary)
            return
        }
        val context = AttackContext(
            enemies = Model.player.militaries.totalAmount * Random.nextDouble(0.4, 1.1)
        )
        Model.currentView = AttackView(context)
        doBeginAttack(context)
    }

    private fun doBeginAttack(context: AttackContext) {
        log.debug { "begin attack: $context" }
        Thread(AttackThread(context, renderer), "Attack-${++threadId}").start()
    }

    // FIXME input amount can be entered as "1k"
    override fun doHire(militaryUnit: Military, amount: Long) {
        log.debug { "want to hire: $amount $militaryUnit" }
        val totalPrice = militaryUnit.buyPrice * amount
        val totalPeople = militaryUnit.costsPeople * amount
        if (isValid(totalPrice, totalPeople, amount)) {
            militaryUnit.amount += amount
            Model.gold -= totalPrice
            Model.people -= totalPeople
            Model.currentView = MilitaryView()
        }
    }

    private fun isValid(totalPrice: Amount, totalPeople: Amount, amount: Long) = validateChoice(listOf(
        SimpleChoiceValidation(
            condition = { Model.gold >= totalPrice },
            alertType = AlertType.NotEnoughGold
        ),
        SimpleChoiceValidation(
            condition = { Model.people >= totalPeople },
            alertType = AlertType.NotEnoughPeople
        ),
        SimpleChoiceValidation(
            condition = { Model.totalMilitaryCapacity >= amount },
            alertType = AlertType.NotEnoughCapacity
        )
    ))

}
