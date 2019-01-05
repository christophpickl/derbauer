package com.github.christophpickl.derbauer.action.throneroom

interface ThroneRoomVisitor<C : ThroneRoomChoice> {
    val choosePrompt: String
    val message: String
    val choices: List<C>
    fun condition(): Boolean
    fun choose(choice: C): String
}
