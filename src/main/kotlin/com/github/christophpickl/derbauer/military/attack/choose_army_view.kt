package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.derbauer.ui.Alert
import com.github.christophpickl.derbauer.ui.AlertType
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.InputView
import mu.KotlinLogging.logger

class ChooseAttackArmyView(
    private val context: PrepareAttackContext
) : InputView(
    message = buildMessage(context),
    cancelSupport = CancelSupport.Disabled
) {

    companion object {
        private fun buildMessage(context: PrepareAttackContext) =
            "Choose your attacking armies:\n" + (if (context.armies.any { it.value != null }) "\n" else "") +
                context.armies.filter { it.value != null }
                    .map { "- ${it.value!!.formatted} ${it.key.labelPlural.capitalize()}" }.joinToString("\n") + "\n" +
                "-> ${context.current.key.labelPlural.capitalize()} available: ${context.current.key.amount.formatted}"
    }

    private val log = logger {}

    override fun onCallback(callback: ViewCallback, number: Long) {
        log.debug { "choosen army ${context.current.key.label} => number: $number" }
        if (number > context.current.key.amount.rounded) {
            Alert.show(AlertType.NotEnoughArmies)
            return
        }

        context.setCurrentAmount(Amount(number))
        if (context.isDone()) {
            callback.onAttackArmyChosen(context.armies.mapValues { it.value!! }.filter { it.value.isNotZero })
        } else {
            context.next()
            Model.currentView = ChooseAttackArmyView(context)
        }
    }

}
