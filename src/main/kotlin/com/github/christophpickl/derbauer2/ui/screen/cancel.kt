package com.github.christophpickl.derbauer2.ui.screen

import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.beep

sealed class CancelSupport {
    object Disabled : CancelSupport()
    class Enabled(val targetScreen: () -> Screen) : CancelSupport()
}

fun handleCancel() {
    when (val cancel = Model.screen.cancelSupport) {
        CancelSupport.Disabled -> beep("Cancel support is disabled")
        is CancelSupport.Enabled -> {
            Model.screen = cancel.targetScreen()
        }
    }
}
