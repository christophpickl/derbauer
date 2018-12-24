package com.github.christophpickl.derbauer2.military

import com.github.christophpickl.derbauer2.misc.SimpleChoiceValidation
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.misc.validateChoice
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.AlertType
import com.github.christophpickl.derbauer2.ui.Renderer
import mu.KotlinLogging.logger

class MilitaryController(
    private val renderer: Renderer
) : MilitaryCallback {
    
    private val log = logger {}

    override fun onMilitary(choice: MilitaryChoice) {
        log.debug { "military action: $choice" }
        when (choice.enum) {
            MilitaryEnum.Attack -> {
                Model.screen = AttackScreen()
            }
            MilitaryEnum.RecruiteSoldiers -> {
                Model.screen = HireSoldiersScreen()

            }
        }.enforceWhenBranches()
    }

    override fun doBeginAttack(context: AttackContext) {
        Thread(AttackThread(context, renderer)).start()
    }


    override fun doHire(militaryUnit: Military, amount: Int) {
        log.debug { "want to hire: $amount $militaryUnit" }
        val totalPrice = militaryUnit.buyPrice * amount
        val totalPeople = militaryUnit.costsPeople * amount
        if (isValid(totalPrice, totalPeople)) {
            militaryUnit.amount += amount
            Model.gold -= totalPrice
            Model.people -= totalPeople
            Model.goHome()
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
