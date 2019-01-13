package com.github.christophpickl.derbauer.ui.view

import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.Beeper
import com.github.christophpickl.derbauer.ui.RealBeeper

interface CancelHandler {
    val isCancelEnabled: Boolean
    fun handleCancel()
}

class CancelHandlerDelegate(
    private val cancelSupport: CancelSupport,
    private val beeper: Beeper = RealBeeper
) : CancelHandler {

    override val isCancelEnabled: Boolean = cancelSupport is CancelSupport.Enabled
    
    override fun handleCancel() {
        when (val cancel = cancelSupport) {
            CancelSupport.Disabled -> beeper.beep("Cancel support is disabled")
            is CancelSupport.Enabled -> {
                Model.currentView = cancel.targetView()
            }
        }
    }
}

sealed class CancelSupport {
    object Disabled : CancelSupport()
    class Enabled(val targetView: () -> View) : CancelSupport()
}
