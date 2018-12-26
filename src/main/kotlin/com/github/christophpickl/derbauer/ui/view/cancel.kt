package com.github.christophpickl.derbauer.ui.view

import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.beep

sealed class CancelSupport {
    object Disabled : CancelSupport()
    class Enabled(val targetView: () -> View) : CancelSupport()
}

fun handleCancel() {
    when (val cancel = Model.currentView.cancelSupport) {
        CancelSupport.Disabled -> beep("Cancel support is disabled")
        is CancelSupport.Enabled -> {
            Model.currentView = cancel.targetView()
        }
    }
}
