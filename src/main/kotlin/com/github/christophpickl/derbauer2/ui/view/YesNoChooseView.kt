package com.github.christophpickl.derbauer2.ui.view

import com.github.christophpickl.derbauer2.ViewCallback

open class YesNoChooseView(
    private val yesNoCallback: YesNoCallback,
    message: String
) : ChooseView<YesNoChoice>(
    messages = listOf(message),
    choices = listOf(EnumChoice(YesNo.Yes, "Yes"), EnumChoice(YesNo.No, "No"))
) {
    override val cancelSupport = CancelSupport.Disabled
    override fun onCallback(callback: ViewCallback, choice: YesNoChoice) {
        yesNoCallback.onYesNo(choice.enum)
    }
}

enum class YesNo {
    Yes,
    No
}

typealias YesNoChoice = EnumChoice<YesNo>


interface YesNoCallback {
    fun onYesNo(choice: YesNo)
}
