package com.github.christophpickl.derbauer2.misc

import com.github.christophpickl.derbauer2.ui.Alert
import com.github.christophpickl.derbauer2.ui.AlertType

fun validateChoice(validations: List<ChoiceValidation>): Boolean {
    val failedValidations = validations.filter { !it.condition() }
    if (failedValidations.isEmpty()) {
        return true
    }
    // showing only first is good enough :) dont bother user too much
    Alert.show(failedValidations.first().alertType)
    return false
}

interface ChoiceValidation {
    val condition: () -> Boolean
    val alertType: AlertType
}

class SimpleChoiceValidation(
    override val condition: () -> Boolean,
    override val alertType: AlertType
) : ChoiceValidation
