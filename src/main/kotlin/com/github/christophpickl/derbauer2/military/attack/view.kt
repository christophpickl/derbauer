package com.github.christophpickl.derbauer2.military.attack

import com.github.christophpickl.derbauer2.ViewCallback
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.PromptInput
import com.github.christophpickl.derbauer2.ui.PromptMode
import com.github.christophpickl.derbauer2.ui.view.CancelSupport
import com.github.christophpickl.derbauer2.ui.view.View

class AttackView(
    private val context: AttackContext
) : View {

    override val promptMode get() = if (context.attackOver) PromptMode.Enter else PromptMode.Off
    override val cancelSupport = CancelSupport.Disabled
    override val renderContent get() = context.message

    override fun onCallback(callback: ViewCallback, input: PromptInput) {
        Model.goHome()
    }
}
