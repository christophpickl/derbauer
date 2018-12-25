package com.github.christophpickl.derbauer2.military

import com.github.christophpickl.derbauer2.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.misc.validateChoice
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.Alert
import com.github.christophpickl.derbauer2.ui.AlertType
import com.github.christophpickl.derbauer2.ui.Renderer
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
        if (Model.player.militaries.totalAmount == 0) {
            Alert.show(AlertType.NoMilitary)
            return
        }
        val context = AttackContext(
            enemies = (Random.nextDouble(0.4, 1.1) * Model.player.militaries.totalAmount).toInt()
        )
        Model.currentView = AttackView(context)
        doBeginAttack(context)
    }

    private fun doBeginAttack(context: AttackContext) {
        log.debug { "begin attack: $context" }
        Thread(AttackThread(context, renderer), "Attack-${++threadId}").start()
    }

    override fun doHire(militaryUnit: Military, amount: Int) {
        log.debug { "want to hire: $amount $militaryUnit" }
        val totalPrice = militaryUnit.buyPrice * amount
        val totalPeople = if (militaryUnit is PeopleMilitary) militaryUnit.costsPeople * amount else 0
        if (isValid(totalPrice, totalPeople)) {
            militaryUnit.amount += amount
            Model.gold -= totalPrice
            Model.people -= totalPeople
            Model.currentView = MilitaryView()
        }
    }

    private fun isValid(totalPrice: Int, totalPeople: Int) = validateChoice(listOf(
        SimpleChoiceValidation(
            condition = { Model.gold >= totalPrice },
            alertType = AlertType.NotEnoughGold
        ),
        SimpleChoiceValidation(
            condition = { Model.people >= totalPeople },
            alertType = AlertType.NotEnoughPeople
        )
    ))

}
