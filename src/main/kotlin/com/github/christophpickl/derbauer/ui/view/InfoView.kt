package com.github.christophpickl.derbauer.ui.view

import com.github.christophpickl.derbauer.ui.Beeper
import com.github.christophpickl.derbauer.ui.PromptMode
import com.github.christophpickl.derbauer.ui.RealBeeper

abstract class InfoView(
    message: String,
    beeper: Beeper = RealBeeper,
    cancelHandler: CancelHandler = CancelHandlerDelegate(CancelSupport.Disabled, beeper)
) : View, CancelHandler by cancelHandler {
    
    override val renderContent = message
    override val promptMode = PromptMode.Enter

}
