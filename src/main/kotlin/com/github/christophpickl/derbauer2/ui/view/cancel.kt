package com.github.christophpickl.derbauer2.ui.view

import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.beep

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
