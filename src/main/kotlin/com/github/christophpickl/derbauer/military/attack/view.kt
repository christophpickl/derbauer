package com.github.christophpickl.derbauer.military.attack

import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.military.MilitaryView
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.PromptInput
import com.github.christophpickl.derbauer.ui.PromptMode
import com.github.christophpickl.derbauer.ui.view.CancelHandler
import com.github.christophpickl.derbauer.ui.view.CancelHandlerDelegate
import com.github.christophpickl.derbauer.ui.view.CancelSupport
import com.github.christophpickl.derbauer.ui.view.View

class AttackView(

    private val context: AttackContext,
    // have to register cancel that way, as the AttackView is somehow "special"
    private val cancelHandler: CancelHandler = CancelHandlerDelegate(cancelSupport = CancelSupport.Disabled)

) : View, CancelHandler by cancelHandler {

    override val promptMode get() = if (context.isAttackOver) PromptMode.Enter else PromptMode.Off
    override val renderContent get() = context.message

    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        Model.currentView = MilitaryView()
    }
}
