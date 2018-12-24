package com.github.christophpickl.derbauer2.misc

import com.github.christophpickl.derbauer2.ui.Alert
import com.github.christophpickl.derbauer2.ui.AlertType
import com.github.christophpickl.derbauer2.ui.screen.Choice

fun <C : Choice> validateChoice(choice: C, validations: List<ChoiceValidation<C>>): Boolean {
    val failedValidations = validations.filter { !it.condition(choice) }
    if (failedValidations.isEmpty()) {
        return true
    }
    // MINOR somehow display all
    Alert.show(failedValidations.first().alertType)
    return false
}

interface ChoiceValidation<C : Choice> {

    val condition: (choice: C) -> Boolean
    val alertType: AlertType
}

class SimpleChoiceValidation<C : Choice>(
    override val condition: (choice: C) -> Boolean,
    override val alertType: AlertType
) : ChoiceValidation<C>
