package com.github.christophpickl.derbauer.misc

import com.github.christophpickl.derbauer.ui.Alert
import com.github.christophpickl.derbauer.ui.AlertType
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

fun validateChoice(validations: List<ChoiceValidation>): Boolean {
    val failedValidations = validations.filter { !it.condition() }
    if (failedValidations.isEmpty()) {
        log.trace { "validation succeeded" }
        return true
    }
    log.trace { "validation failed: ${failedValidations.joinToString() { it.alertType.message }}" }
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
