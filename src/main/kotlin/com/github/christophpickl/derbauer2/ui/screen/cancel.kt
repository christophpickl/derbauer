package com.github.christophpickl.derbauer2.ui.screen

import com.github.christophpickl.derbauer.logic.beep
import com.github.christophpickl.derbauer2.model.Model

sealed class CancelSupport {
    object Disabled : CancelSupport()
    class Enabled(val targetScreen: () -> Screen) : CancelSupport()
}

fun handleCancel() {
    when (val cancel = Model.screen.cancelSupport) {
        CancelSupport.Disabled -> beep()
        is CancelSupport.Enabled -> {
            Model.screen = cancel.targetScreen()
        }
    }
}
